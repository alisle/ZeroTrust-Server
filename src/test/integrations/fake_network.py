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
    }
]

CONNECTIONS = []

print "Welcome to the Fake Network!"

def create_machine_names():
    names = []
    with open("./resources/names.txt") as file_pointer:
        for line in file_pointer:
            names.append(line.strip())
    return names


def unleash_agents(number_of_agents, names, headers):
    samples = random.sample(range(0, len(names)), number_of_agents)

    agents = []

    for sample in samples:
        agent_uuid = uuid.uuid4()
        agent_name = names[sample]
        agent_online = { 'name' : agent_name, 'uuid' : str(agent_uuid) }
        agent_online_response = requests.post(SERVER + "/agents/online", json=agent_online, headers=headers)
        if agent_online_response.status_code != 200:
            print "Unable to set agent online", agent_online_response.status_code
        else:
            agent = {}
            agent["name"] = agent_name
            agent["uuid"] = agent_uuid
            agent["ip"] = "10.0." + str(random.randint(0, 254)) + "." + str(random.randint(0, 254))

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
    payload["source"] = source_agent["ip"]
    payload["source_port"] = random.randint(2000, 60000)
    payload["destination"] = destination_agent["ip"]
    payload["destination_port"] = service["destination_port"]

    return payload


def post_open(is_source, payload, timestamp, service, headers):
    payload["timestamp"] = timestamp + "+00:00"

    if is_source:
        payload["username"] = "harry"
        payload["uid"] = 1001
    else:
        payload["username"] = service["destination_user_name"]
        payload["uid"] = service["destination_uid"]

    payload["program_details"] = create_program_source(is_source, service, payload["source"], payload["destination"])

    response = requests.post(SERVER + "/connections/open", json=payload, headers=headers)
    if response.status_code != 200:
        print "Unable to post connection", response.status_code


def post_close(payload, timestamp, headers):
    payload["timestamp"] = timestamp + "+00:00"
    response = requests.post(SERVER + "/connections/close", json=payload, headers=headers)
    if response.status_code != 200:
        print "Unable to post connection", response.status_code


def make_connection(source_agent, destination_agent, service, duration, headers):
    timestamp_start = datetime.now()
    timestamp_end = timestamp_start + timedelta(seconds=duration)
    base = connection_base(source_agent, destination_agent, service)

    post_open(True, copy.deepcopy(base), timestamp_start.isoformat(), service, headers)
    post_close(copy.deepcopy(base), timestamp_end.isoformat(), headers)

    base["agent"] = str(destination_agent["uuid"])
    base["uuid"] = str(uuid.uuid4())

    post_open(False, copy.deepcopy(base), timestamp_start.isoformat(), service, headers)
    post_close(copy.deepcopy(base), timestamp_end.isoformat(), headers)


print "Loading Machine Names"
names = create_machine_names()

print "Onlining Agents"
agents = unleash_agents(100, names, HEADERS)

while True:
    sample = random.sample(range(0, len(agents)), 2)
    source_agent = agents[sample[0]]
    destination_agent = agents[sample[1]]
    print "Creating connection between "  + str(source_agent["uuid"]) + " -> " + str(destination_agent["uuid"])
    make_connection(source_agent, destination_agent, SERVICES[0], random.randint(0, 10800), HEADERS)
    time.sleep(1)
