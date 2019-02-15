#!/usr/bin/python

from datetime import datetime, timedelta
import random
import requests
import time
import uuid
import copy
import pprint


SERVER = "http://localhost:8080"
HEADERS = { "Content-Type": "application/json" }

SERVICES = [
    {
        "source_process_name": "ssh",
        "source_command_line": [ "/usr/bin/ssh", "%DEST_IP%" ],
        "destination_process_name": "sshd",
        "destination_command_line": [ "/usr/sbin/sshd", "[accepted]" ],
        "destination_user_name": "root",
        "destination_port": 22,
        "destination_uid": 0,
        "protocol" : "TCP"
    },
    {
        "source_process_name": "psql",
        "source_command_line" : [ "/usr/lib/postgresql/9.4/bin/psql", "-h", "%DEST_IP%"],
        "destination_process_name" : "postgres",
        "destination_command_line" : [ "/usr/lib/postgresql/9.4/bin/postgres", "-D", "/var/lib/postgresql/9.4/main", "-c", "config_file=/etc/postgresql/9.4/main/postgresql.conf" ],
        "destination_user_name" : "postgres",
        "destination_port" : 5432,
        "destination_uid" : 108,
        "protocol": "TCP"
    }
]

CONNECTIONS = {}


print "Welcome to the Fake Network!"

def create_machine_names():
    names = []
    with open("./resources/names.txt") as file_pointer:
        for line in file_pointer:
            names.append(line.strip())
    return names


def create_usernames() :
    usernames = []
    with open("./resources/usernames.txt") as file_pointer:
        for line in file_pointer:
            usernames.append(line.strip().lower())

    return usernames

def unleash_agents(number_of_agents, names, headers):
    samples = random.sample(range(0, len(names)), number_of_agents)

    agents = []

    for sample in samples:
        agent_uuid = uuid.uuid4()
        agent_name = names[sample]
        agent_ip = "10.0." + str(random.randint(0, 254)) + "." + str(random.randint(0, 254))
        agent_online = { 'name' : agent_name, 'uuid' : str(agent_uuid), 'interfaces': [ agent_ip ] }
        agent_online_response = requests.post(SERVER + "/agents/online", json=agent_online, headers=headers)
        if agent_online_response.status_code != 200:
            print "Unable to set agent online", agent_online_response.status_code
        else:
            agent = {}
            agent["name"] = agent_name
            agent["uuid"] = agent_uuid
            agent["ip"] = agent_ip
            agents.append(agent)
    return agents

def create_program_source(is_source, service, source, destination):
    program = {}
    program["inode"] = random.randint(700000, 1000000)
    program["pid"] = random.randint(100, 10000)

    if is_source:
        program["process_name"] = service["source_process_name"]
        program["command_line"] = list(service["source_command_line"])
    else:
        program["process_name"] = service["destination_process_name"]
        program["command_line"] = list(service["destination_command_line"])

    # Do replacement gubbings
    for pos, word in enumerate(program["command_line"]):
        if word == "%DEST_IP%":
            program["command_line"][pos] = destination

    return program

def connection_base(source_agent, destination_agent, service):
    payload = {}
    payload["hash"] = random.getrandbits(64) - 2**63
    payload["agent"] = str(source_agent["uuid"])
    payload["uuid"] = str(uuid.uuid4())
    payload["protocol"] = service["protocol"]
    payload["sourceString"] = source_agent["ip"]
    payload["source_port"] = random.randint(2000, 60000)
    payload["destinationString"] = destination_agent["ip"]
    payload["destination_port"] = service["destination_port"]

    return payload


def post_open(is_source, payload, timestamp, service, headers):
    payload["timestamp"] = timestamp + "+00:00"

    if is_source:
        sample = random.sample(range(0, len(usernames)), 1)
        payload["username"] = usernames[sample[0]]
        payload["uid"] = 1001
    else:
        payload["username"] = service["destination_user_name"]
        payload["uid"] = service["destination_uid"]

    payload["program_details"] = create_program_source(is_source, service, payload["sourceString"], payload["destinationString"])

    response = requests.post(SERVER + "/connections/open", json=payload, headers=headers)
    if response.status_code != 200:
        print "Unable to post connection", response.status_code

    return payload

def post_close(payload, headers):
    response = requests.post(SERVER + "/connections/close", json=payload, headers=headers)
    if response.status_code != 200:
        print "Unable to post connection", response.status_code

    return payload



def make_connection(source_agent, destination_agent, service, ticker, duration, headers):
    end_point = ticker * 1000 + duration

    timestamp_start = datetime.now()
    timestamp_end = timestamp_start + timedelta(seconds=duration)
    base = connection_base(source_agent, destination_agent, service)


    post_open(True, copy.deepcopy(base), timestamp_start.isoformat(), service, headers)
    source_payload_close = copy.deepcopy(base)
    source_payload_close["timestamp"] = timestamp_end.isoformat() + "+00:00"

    base["agent"] = str(destination_agent["uuid"])
    base["uuid"] = str(uuid.uuid4())

    post_open(False, copy.deepcopy(base), timestamp_start.isoformat(), service, headers)
    destination_payload_close = copy.deepcopy(base)
    destination_payload_close["timestamp"] = timestamp_end.isoformat() + "+00:00"

    if end_point not in CONNECTIONS:
        CONNECTIONS[end_point] = []

    CONNECTIONS[end_point].append({ "source": source_payload_close, "destination":destination_payload_close})

def process_connections(ticker, headers):
    current = ticker * 1000
    dead_markers = []
    for end_point in CONNECTIONS:
        if end_point < current:
            for connection in CONNECTIONS[end_point]:
                print "Closing connection between " + str(connection["source"]["agent"]) + " -> " + str(connection["destination"]["agent"])
                post_close(connection["source"], headers)
                post_close(connection["destination"], headers)

            dead_markers.append(end_point)

    for marker in dead_markers:
        del CONNECTIONS[marker]

print "Loading Machine Names"
names = create_machine_names()
print "Loaded " + str(len(names)) + " machine names"

print "Loading Usernames"
usernames = create_usernames()
print "Loaded " + str(len(usernames)) + " usernames"

print "Online Agents"
agents = unleash_agents(10, names, HEADERS)

ticker = 0
while True:
    ticker += 1
    sample = random.sample(range(0, len(agents)), 2)
    process_sample = random.sample(range(0, len(SERVICES)), 1)

    source_agent = agents[sample[0]]
    destination_agent = agents[sample[1]]
    print "Creating connection between "  + str(source_agent["uuid"]) + " -> " + str(destination_agent["uuid"])
    make_connection(source_agent, destination_agent, SERVICES[process_sample[0]], ticker, random.randint(0, 10800), HEADERS)
    process_connections(ticker, HEADERS)
    time.sleep(1)
