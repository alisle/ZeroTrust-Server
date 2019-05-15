INSERT INTO PUBLIC.IP_ADDRESS(ADDRESS, ADDRESS_STRING, VERSION) VALUES
(167782697, '10.0.41.41', 'V4'),
(167798336, '10.0.102.64', 'V4'),
(167804391, '10.0.125.231', 'V4'),
(167804870, '10.0.127.198', 'V4'),
(167813943, '10.0.163.55', 'V4'),
(167818874, '10.0.182.122', 'V4'),
(167828703, '10.0.220.223', 'V4'),
(167832238, '10.0.234.174', 'V4'),
(167835007, '10.0.245.127', 'V4'),
(167835471, '10.0.247.79', 'V4');

INSERT INTO PUBLIC.AGENT(ID, NAME, FIRST_SEEN, LAST_SEEN, ALIVE, KNOWN) VALUES
('afdbbc76-5dc9-452c-a248-45cb5aa0f769', 'Gitega', TIMESTAMP '2019-04-15 15:12:26.589382', TIMESTAMP '2019-04-15 15:12:54.966821', TRUE, TRUE),
('0aef9c8d-18f4-4623-b271-d576fe0467a6', 'Wellington', TIMESTAMP '2019-04-15 15:12:26.640348', TIMESTAMP '2019-04-15 15:12:51.793295', TRUE, TRUE),
('e6d8bf5c-a70d-46d7-8e50-3f9cfc30938e', 'Kathmandu', TIMESTAMP '2019-04-15 15:12:26.655656', TIMESTAMP '2019-04-15 15:12:54.937448', TRUE, TRUE),
('b2258d50-68bd-4f97-9e17-af128be6a9bb', 'Port-Vila', TIMESTAMP '2019-04-15 15:12:26.668025', TIMESTAMP '2019-04-15 15:12:48.718688', TRUE, TRUE),
('4f3e863b-72bc-4993-83f6-0242bbdbb467', 'Panama City', TIMESTAMP '2019-04-15 15:12:26.680073', TIMESTAMP '2019-04-15 15:12:50.77066', TRUE, TRUE),
('713b49f6-a676-4e1b-bec7-aba2a0010b08', 'Moroni', TIMESTAMP '2019-04-15 15:12:26.693004', TIMESTAMP '2019-04-15 15:12:54.99458', TRUE, TRUE),
('1042530b-5d04-45ca-b225-d50e9f3503ab', 'Ljubljana', TIMESTAMP '2019-04-15 15:12:26.704623', TIMESTAMP '2019-04-15 15:12:45.64514', TRUE, TRUE),
('f46c1bbf-71f5-4c23-a211-c0a2dacf8c24', 'Abidjan', TIMESTAMP '2019-04-15 15:12:26.717431', TIMESTAMP '2019-04-15 15:12:52.860247', TRUE, TRUE),
('e282a292-a759-46b9-a0fe-5f351cffa5f6', 'Montevideo', TIMESTAMP '2019-04-15 15:12:26.728346', TIMESTAMP '2019-04-15 15:12:54.921782', TRUE, TRUE),
('83fe17cc-2017-4f51-a370-9b8cca550025', 'Palikir', TIMESTAMP '2019-04-15 15:12:26.737712', TIMESTAMP '2019-04-15 15:12:37.348151', TRUE, TRUE);

INSERT INTO PUBLIC.AGENT_IP_ADDRESS(AGENT_ID, IP_ADDRESS) VALUES
('afdbbc76-5dc9-452c-a248-45cb5aa0f769', 167798336),
('0aef9c8d-18f4-4623-b271-d576fe0467a6', 167782697),
('e6d8bf5c-a70d-46d7-8e50-3f9cfc30938e', 167828703),
('b2258d50-68bd-4f97-9e17-af128be6a9bb', 167804391),
('4f3e863b-72bc-4993-83f6-0242bbdbb467', 167804870),
('713b49f6-a676-4e1b-bec7-aba2a0010b08', 167832238),
('1042530b-5d04-45ca-b225-d50e9f3503ab', 167813943),
('f46c1bbf-71f5-4c23-a211-c0a2dacf8c24', 167818874),
('e282a292-a759-46b9-a0fe-5f351cffa5f6', 167835007),
('83fe17cc-2017-4f51-a370-9b8cca550025', 167835471);

INSERT INTO PUBLIC.AGENT_NETWORK(AGENT_ID, NETWORK_ID) VALUES
('afdbbc76-5dc9-452c-a248-45cb5aa0f769', '59ed5983-da6a-4036-b2e9-9c613594ddb9'),
('0aef9c8d-18f4-4623-b271-d576fe0467a6', '59ed5983-da6a-4036-b2e9-9c613594ddb9'),
('e6d8bf5c-a70d-46d7-8e50-3f9cfc30938e', '59ed5983-da6a-4036-b2e9-9c613594ddb9'),
('b2258d50-68bd-4f97-9e17-af128be6a9bb', '59ed5983-da6a-4036-b2e9-9c613594ddb9'),
('4f3e863b-72bc-4993-83f6-0242bbdbb467', '59ed5983-da6a-4036-b2e9-9c613594ddb9'),
('713b49f6-a676-4e1b-bec7-aba2a0010b08', '59ed5983-da6a-4036-b2e9-9c613594ddb9'),
('1042530b-5d04-45ca-b225-d50e9f3503ab', '59ed5983-da6a-4036-b2e9-9c613594ddb9'),
('f46c1bbf-71f5-4c23-a211-c0a2dacf8c24', '59ed5983-da6a-4036-b2e9-9c613594ddb9'),
('e282a292-a759-46b9-a0fe-5f351cffa5f6', '59ed5983-da6a-4036-b2e9-9c613594ddb9'),
('83fe17cc-2017-4f51-a370-9b8cca550025', '59ed5983-da6a-4036-b2e9-9c613594ddb9');              

INSERT INTO PUBLIC.CONNECTION(ID, CONNECTION_HASH, AGENT_ID, CONNECTION_STARTED, CONNECTION_ENDED, DURATION, PROTOCOL, SOURCE_ADDRESS, SOURCE_ADDRESS_STRING, SOURCE_NETWORK_ID, DESTINATION_ADDRESS, DESTINATION_ADDRESS_STRING, DESTINATION_NETWORK_ID, SOURCE_PORT, DESTINATION_PORT, USERNAME, UID, INODE, PID, PROCESS_NAME, COMMAND_LINE) VALUES
('6ae47a0c-94f8-44c4-9074-79aca8ce79ab', 6154602466870092764, '0aef9c8d-18f4-4623-b271-d576fe0467a6', TIMESTAMP '2019-04-15 15:12:26.744211', TIMESTAMP '2019-04-15 15:12:30.744211', 4000, '0', 167782697, '10.0.41.41', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 167835471, '10.0.247.79', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 12937, 5432, 'mia', 1001, 810827, 9665, 'psql', '/usr/lib/postgresql/9.4/bin/psql -h 10.0.247.79'),
('13e0f7bf-40a7-4c49-a711-80f8e92f994d', 6154602466870092764, '83fe17cc-2017-4f51-a370-9b8cca550025', TIMESTAMP '2019-04-15 15:12:26.744211', TIMESTAMP '2019-04-15 15:12:30.744211', 4000, '0', 167782697, '10.0.41.41', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 167835471, '10.0.247.79', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 12937, 5432, 'postgres', 108, 705987, 461, 'postgres', '/usr/lib/postgresql/9.4/bin/postgres -D /var/lib/postgresql/9.4/main -c config_file=/etc/postgresql/9.4/main/postgresql.conf'),
('42c5e1f9-25cb-425e-8c31-d7c8c0b72fd8', 2417520910791374621, '0aef9c8d-18f4-4623-b271-d576fe0467a6', TIMESTAMP '2019-04-15 15:12:27.857681', NULL, 0, '0', 167782697, '10.0.41.41', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 167804870, '10.0.127.198', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 53546, 22, 'jessica', 1001, 929017, 8301, 'ssh', '/usr/bin/ssh 10.0.127.198'),
('89996c58-102c-4076-a4ac-ead8506c05a6', 2417520910791374621, '4f3e863b-72bc-4993-83f6-0242bbdbb467', TIMESTAMP '2019-04-15 15:12:27.857681', NULL, 0, '0', 167782697, '10.0.41.41', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 167804870, '10.0.127.198', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 53546, 22, 'root', 0, 865009, 9101, 'sshd', '/usr/sbin/sshd [accepted]'),
('b913fcfc-ddf4-4188-bcb9-1dea75770a62', 5148955430400096458, 'e282a292-a759-46b9-a0fe-5f351cffa5f6', TIMESTAMP '2019-04-15 15:12:28.935277', NULL, 0, '0', 167835007, '10.0.245.127', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 122135634, '7.71.164.82', '00000000-0000-0000-0000-000000000000', 41997, 443, 'daisy', 1001, 907819, 7894, 'chrome', '/opt/google/chrome/chrome'),
('3038d988-32d7-4c00-b2d4-5e35879d97a9', 4454544584831484664, 'f46c1bbf-71f5-4c23-a211-c0a2dacf8c24', TIMESTAMP '2019-04-15 15:12:29.958112', NULL, 0, '0', 167818874, '10.0.182.122', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 167813943, '10.0.163.55', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 50042, 5432, 'harper', 1001, 986703, 4606, 'psql', '/usr/lib/postgresql/9.4/bin/psql -h 10.0.163.55'),
('e6e1f0b7-c591-42c2-bdc4-5c5dcb58d60c', 4454544584831484664, '1042530b-5d04-45ca-b225-d50e9f3503ab', TIMESTAMP '2019-04-15 15:12:29.958112', NULL, 0, '0', 167818874, '10.0.182.122', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 167813943, '10.0.163.55', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 50042, 5432, 'postgres', 108, 871356, 2630, 'postgres', '/usr/lib/postgresql/9.4/bin/postgres -D /var/lib/postgresql/9.4/main -c config_file=/etc/postgresql/9.4/main/postgresql.conf'),
('1cfd0454-6e33-4f2d-8b21-a03346a24e8c', 2348118243233934881, 'e282a292-a759-46b9-a0fe-5f351cffa5f6', TIMESTAMP '2019-04-15 15:12:30.992693', NULL, 0, '0', 167835007, '10.0.245.127', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 167798336, '10.0.102.64', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 29371, 22, 'harper', 1001, 994506, 9829, 'ssh', '/usr/bin/ssh 10.0.102.64'),
('485330bb-91c3-4100-af6d-0ae3d2a396ea', 2348118243233934881, 'afdbbc76-5dc9-452c-a248-45cb5aa0f769', TIMESTAMP '2019-04-15 15:12:30.992693', NULL, 0, '0', 167835007, '10.0.245.127', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 167798336, '10.0.102.64', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 29371, 22, 'root', 0, 887839, 2000, 'sshd', '/usr/sbin/sshd [accepted]'),
('ee1c9cb5-e76e-4a87-a3aa-ae2654517d80', 2053105620372032212, 'b2258d50-68bd-4f97-9e17-af128be6a9bb', TIMESTAMP '2019-04-15 15:12:32.087332', TIMESTAMP '2019-04-15 15:12:36.087332', 4000, '0', 167804391, '10.0.125.231', '59ed5983-da6a-4036-b2e9-9c613594ddb9', -1792940002, '149.33.232.30', '00000000-0000-0000-0000-000000000000', 38177, 443, 'sienna', 1001, 791797, 798, 'chrome', '/opt/google/chrome/chrome');

INSERT INTO PUBLIC.CONNECTION(ID, CONNECTION_HASH, AGENT_ID, CONNECTION_STARTED, CONNECTION_ENDED, DURATION, PROTOCOL, SOURCE_ADDRESS, SOURCE_ADDRESS_STRING, SOURCE_NETWORK_ID, DESTINATION_ADDRESS, DESTINATION_ADDRESS_STRING, DESTINATION_NETWORK_ID, SOURCE_PORT, DESTINATION_PORT, USERNAME, UID, INODE, PID, PROCESS_NAME, COMMAND_LINE) VALUES
('aaf5d9fa-ea80-4b3a-bd71-9f90fe036246', -2493897573505804936, 'f46c1bbf-71f5-4c23-a211-c0a2dacf8c24', TIMESTAMP '2019-04-15 15:12:33.123698', NULL, 0, '0', 167818874, '10.0.182.122', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 167804870, '10.0.127.198', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 38501, 22, 'freya', 1001, 975706, 3985, 'ssh', '/usr/bin/ssh 10.0.127.198'),
('7bc340a5-efd1-4901-8f2b-050dc15fb851', -2493897573505804936, '4f3e863b-72bc-4993-83f6-0242bbdbb467', TIMESTAMP '2019-04-15 15:12:33.123698', NULL, 0, '0', 167818874, '10.0.182.122', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 167804870, '10.0.127.198', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 38501, 22, 'root', 0, 766879, 5016, 'sshd', '/usr/sbin/sshd [accepted]'),
('cc8cc572-096c-4ea4-9106-948dd44e586a', 6016087765958840248, 'b2258d50-68bd-4f97-9e17-af128be6a9bb', TIMESTAMP '2019-04-15 15:12:34.190913', NULL, 0, '0', 167804391, '10.0.125.231', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 167835007, '10.0.245.127', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 7385, 5432, 'poppy', 1001, 942985, 6091, 'psql', '/usr/lib/postgresql/9.4/bin/psql -h 10.0.245.127'),
('d76d0a6a-1194-4cd6-b1ec-7c4bd863c57b', 6016087765958840248, 'e282a292-a759-46b9-a0fe-5f351cffa5f6', TIMESTAMP '2019-04-15 15:12:34.190913', NULL, 0, '0', 167804391, '10.0.125.231', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 167835007, '10.0.245.127', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 7385, 5432, 'postgres', 108, 895762, 1671, 'postgres', '/usr/lib/postgresql/9.4/bin/postgres -D /var/lib/postgresql/9.4/main -c config_file=/etc/postgresql/9.4/main/postgresql.conf'),
('480220aa-4556-49db-bd0f-529c7ada4cf0', 4332363873623632806, '1042530b-5d04-45ca-b225-d50e9f3503ab', TIMESTAMP '2019-04-15 15:12:35.223274', NULL, 0, '0', 167813943, '10.0.163.55', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 2004143314, '119.116.204.210', '00000000-0000-0000-0000-000000000000', 3517, 443, 'olivia', 1001, 924676, 9723, 'chrome', '/opt/google/chrome/chrome'),
('fa06b7ef-f7cc-4710-9421-a92c8cf37b21', -7045999014692899530, 'afdbbc76-5dc9-452c-a248-45cb5aa0f769', TIMESTAMP '2019-04-15 15:12:36.242045', NULL, 0, '0', 167798336, '10.0.102.64', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 167782697, '10.0.41.41', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 39871, 22, 'amelia', 1001, 937151, 288, 'ssh', '/usr/bin/ssh 10.0.41.41'),
('3f2d9683-cc63-4816-8005-572ca893b367', -7045999014692899530, '0aef9c8d-18f4-4623-b271-d576fe0467a6', TIMESTAMP '2019-04-15 15:12:36.242045', NULL, 0, '0', 167798336, '10.0.102.64', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 167782697, '10.0.41.41', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 39871, 22, 'root', 0, 738678, 2759, 'sshd', '/usr/sbin/sshd [accepted]'),
('dbddf3ba-41cb-4654-8b48-0b0a445ed445', 7617953247103136494, '4f3e863b-72bc-4993-83f6-0242bbdbb467', TIMESTAMP '2019-04-15 15:12:37.324762', NULL, 0, '0', 167804870, '10.0.127.198', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 167835471, '10.0.247.79', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 59520, 5432, 'jessica', 1001, 815300, 4126, 'psql', '/usr/lib/postgresql/9.4/bin/psql -h 10.0.247.79'),
('9f3d0633-a386-4b1d-93dc-fcd2d0446763', 7617953247103136494, '83fe17cc-2017-4f51-a370-9b8cca550025', TIMESTAMP '2019-04-15 15:12:37.324762', NULL, 0, '0', 167804870, '10.0.127.198', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 167835471, '10.0.247.79', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 59520, 5432, 'postgres', 108, 960952, 6604, 'postgres', '/usr/lib/postgresql/9.4/bin/postgres -D /var/lib/postgresql/9.4/main -c config_file=/etc/postgresql/9.4/main/postgresql.conf'),
('2adf6333-d91a-475e-96b9-0b0a1131b39c', -8906163645830194496, 'afdbbc76-5dc9-452c-a248-45cb5aa0f769', TIMESTAMP '2019-04-15 15:12:38.355214', NULL, 0, '0', 167798336, '10.0.102.64', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 741831390, '44.55.114.222', '00000000-0000-0000-0000-000000000000', 31223, 443, 'freya', 1001, 897702, 2322, 'chrome', '/opt/google/chrome/chrome');

INSERT INTO PUBLIC.CONNECTION(ID, CONNECTION_HASH, AGENT_ID, CONNECTION_STARTED, CONNECTION_ENDED, DURATION, PROTOCOL, SOURCE_ADDRESS, SOURCE_ADDRESS_STRING, SOURCE_NETWORK_ID, DESTINATION_ADDRESS, DESTINATION_ADDRESS_STRING, DESTINATION_NETWORK_ID, SOURCE_PORT, DESTINATION_PORT, USERNAME, UID, INODE, PID, PROCESS_NAME, COMMAND_LINE) VALUES
('1c597fb7-d4c8-40d6-b371-b0b3ef591bc5', 3032480449985072106, 'e282a292-a759-46b9-a0fe-5f351cffa5f6', TIMESTAMP '2019-04-15 15:12:39.393861', NULL, 0, '0', 167835007, '10.0.245.127', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 167798336, '10.0.102.64', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 17142, 22, 'ella', 1001, 886151, 3085, 'ssh', '/usr/bin/ssh 10.0.102.64'),
('b7e04802-97cd-4bd5-80df-a8b33cf904ec', 3032480449985072106, 'afdbbc76-5dc9-452c-a248-45cb5aa0f769', TIMESTAMP '2019-04-15 15:12:39.393861', NULL, 0, '0', 167835007, '10.0.245.127', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 167798336, '10.0.102.64', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 17142, 22, 'root', 0, 715717, 4984, 'sshd', '/usr/sbin/sshd [accepted]'),
('d0c9f707-2ebb-4530-8fae-beb97294a5fa', -3425257438626106169, 'e282a292-a759-46b9-a0fe-5f351cffa5f6', TIMESTAMP '2019-04-15 15:12:40.422363', NULL, 0, '0', 167835007, '10.0.245.127', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 167804391, '10.0.125.231', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 51591, 5432, 'ivy', 1001, 705862, 8912, 'psql', '/usr/lib/postgresql/9.4/bin/psql -h 10.0.125.231'),
('3e03f111-ae5a-45f2-81d6-a1d9fb837919', -3425257438626106169, 'b2258d50-68bd-4f97-9e17-af128be6a9bb', TIMESTAMP '2019-04-15 15:12:40.422363', NULL, 0, '0', 167835007, '10.0.245.127', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 167804391, '10.0.125.231', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 51591, 5432, 'postgres', 108, 917319, 8872, 'postgres', '/usr/lib/postgresql/9.4/bin/postgres -D /var/lib/postgresql/9.4/main -c config_file=/etc/postgresql/9.4/main/postgresql.conf'),
('82dd9b6e-aa14-47da-ad8d-6952217c69d9', 2229469083177408084, '713b49f6-a676-4e1b-bec7-aba2a0010b08', TIMESTAMP '2019-04-15 15:12:41.451405', NULL, 0, '0', 167832238, '10.0.234.174', '59ed5983-da6a-4036-b2e9-9c613594ddb9', -626611614, '218.166.170.98', '00000000-0000-0000-0000-000000000000', 34420, 443, 'mia', 1001, 947795, 7082, 'chrome', '/opt/google/chrome/chrome'),
('d93bc47d-39b4-4684-bec8-0dbdddfb624f', -2605722118715978430, 'afdbbc76-5dc9-452c-a248-45cb5aa0f769', TIMESTAMP '2019-04-15 15:12:42.477487', NULL, 0, '0', 167798336, '10.0.102.64', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 167828703, '10.0.220.223', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 49002, 22, 'poppy', 1001, 811815, 8031, 'ssh', '/usr/bin/ssh 10.0.220.223'),
('656668bc-761c-49f8-8967-cd2ba5ec01f9', -2605722118715978430, 'e6d8bf5c-a70d-46d7-8e50-3f9cfc30938e', TIMESTAMP '2019-04-15 15:12:42.477487', NULL, 0, '0', 167798336, '10.0.102.64', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 167828703, '10.0.220.223', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 49002, 22, 'root', 0, 774349, 8356, 'sshd', '/usr/sbin/sshd [accepted]'),
('29ae7f33-7564-4830-bce5-88a9d9b64c7f', 233254759164713446, '0aef9c8d-18f4-4623-b271-d576fe0467a6', TIMESTAMP '2019-04-15 15:12:43.506692', NULL, 0, '0', 167782697, '10.0.41.41', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 167832238, '10.0.234.174', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 41842, 22, 'isabelle', 1001, 859859, 1405, 'ssh', '/usr/bin/ssh 10.0.234.174'),
('0120fe57-23a6-40b1-8983-caad3115044b', 233254759164713446, '713b49f6-a676-4e1b-bec7-aba2a0010b08', TIMESTAMP '2019-04-15 15:12:43.506692', NULL, 0, '0', 167782697, '10.0.41.41', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 167832238, '10.0.234.174', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 41842, 22, 'root', 0, 789408, 9985, 'sshd', '/usr/sbin/sshd [accepted]'),
('1e980741-74b9-487b-86dd-17fb3ae70646', 5072807480960586777, '0aef9c8d-18f4-4623-b271-d576fe0467a6', TIMESTAMP '2019-04-15 15:12:44.533924', NULL, 0, '0', 167782697, '10.0.41.41', '59ed5983-da6a-4036-b2e9-9c613594ddb9', -1328111221, '176.214.161.139', '00000000-0000-0000-0000-000000000000', 48005, 443, 'isabella', 1001, 806740, 8838, 'chrome', '/opt/google/chrome/chrome');

INSERT INTO PUBLIC.CONNECTION(ID, CONNECTION_HASH, AGENT_ID, CONNECTION_STARTED, CONNECTION_ENDED, DURATION, PROTOCOL, SOURCE_ADDRESS, SOURCE_ADDRESS_STRING, SOURCE_NETWORK_ID, DESTINATION_ADDRESS, DESTINATION_ADDRESS_STRING, DESTINATION_NETWORK_ID, SOURCE_PORT, DESTINATION_PORT, USERNAME, UID, INODE, PID, PROCESS_NAME, COMMAND_LINE) VALUES
('df71ac09-950e-4063-873b-ecb784c316a7', -8328445360121123683, 'e282a292-a759-46b9-a0fe-5f351cffa5f6', TIMESTAMP '2019-04-15 15:12:45.572996', TIMESTAMP '2019-04-15 15:12:45.572996', 0, '0', 167835007, '10.0.245.127', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 167813943, '10.0.163.55', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 8190, 22, 'chloe', 1001, 850836, 7394, 'ssh', '/usr/bin/ssh 10.0.163.55'),
('a1259bcb-94ff-4ef5-b89c-71ef1f079cf5', -8328445360121123683, '1042530b-5d04-45ca-b225-d50e9f3503ab', TIMESTAMP '2019-04-15 15:12:45.572996', TIMESTAMP '2019-04-15 15:12:45.572996', 0, '0', 167835007, '10.0.245.127', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 167813943, '10.0.163.55', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 8190, 22, 'root', 0, 902318, 8367, 'sshd', '/usr/sbin/sshd [accepted]'),
('b54c8f4f-cb4e-409b-9899-af080ea4daf2', 4198137929323018877, '713b49f6-a676-4e1b-bec7-aba2a0010b08', TIMESTAMP '2019-04-15 15:12:46.666848', TIMESTAMP '2019-04-15 15:12:54.666848', 8000, '0', 167832238, '10.0.234.174', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 167798336, '10.0.102.64', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 56468, 22, 'scarlett', 1001, 828830, 6837, 'ssh', '/usr/bin/ssh 10.0.102.64'),
('9a5d7bd7-39be-4888-a85b-dce04c5c899e', 4198137929323018877, 'afdbbc76-5dc9-452c-a248-45cb5aa0f769', TIMESTAMP '2019-04-15 15:12:46.666848', TIMESTAMP '2019-04-15 15:12:54.666848', 8000, '0', 167832238, '10.0.234.174', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 167798336, '10.0.102.64', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 56468, 22, 'root', 0, 968457, 366, 'sshd', '/usr/sbin/sshd [accepted]'),
('ddb73e08-21ab-43e8-aedb-7ccab4cdea2b', 937696346245267234, '0aef9c8d-18f4-4623-b271-d576fe0467a6', TIMESTAMP '2019-04-15 15:12:47.692844', NULL, 0, '0', 167782697, '10.0.41.41', '59ed5983-da6a-4036-b2e9-9c613594ddb9', -1032135777, '194.122.219.159', '00000000-0000-0000-0000-000000000000', 32385, 443, 'jessica', 1001, 937227, 546, 'chrome', '/opt/google/chrome/chrome'),
('adf1f20a-667d-4352-ae1b-5d2fdacaf2a5', 9131331755688410496, 'b2258d50-68bd-4f97-9e17-af128be6a9bb', TIMESTAMP '2019-04-15 15:12:48.705835', NULL, 0, '0', 167804391, '10.0.125.231', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 167818874, '10.0.182.122', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 37369, 22, 'evie', 1001, 948203, 5727, 'ssh', '/usr/bin/ssh 10.0.182.122'),
('5e7cd734-b119-4df0-b053-93bee419a4d1', 9131331755688410496, 'f46c1bbf-71f5-4c23-a211-c0a2dacf8c24', TIMESTAMP '2019-04-15 15:12:48.705835', NULL, 0, '0', 167804391, '10.0.125.231', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 167818874, '10.0.182.122', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 37369, 22, 'root', 0, 881181, 3335, 'sshd', '/usr/sbin/sshd [accepted]'),
('95674412-9e7c-4d5c-b17e-8c7d97b609ed', 1201596231777073575, 'e6d8bf5c-a70d-46d7-8e50-3f9cfc30938e', TIMESTAMP '2019-04-15 15:12:49.733497', NULL, 0, '0', 167828703, '10.0.220.223', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 167798336, '10.0.102.64', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 3879, 5432, 'amelia', 1001, 867713, 3414, 'psql', '/usr/lib/postgresql/9.4/bin/psql -h 10.0.102.64'),
('aaf3147b-5f82-4036-9a32-89ab16047760', 1201596231777073575, 'afdbbc76-5dc9-452c-a248-45cb5aa0f769', TIMESTAMP '2019-04-15 15:12:49.733497', NULL, 0, '0', 167828703, '10.0.220.223', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 167798336, '10.0.102.64', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 3879, 5432, 'postgres', 108, 874000, 6646, 'postgres', '/usr/lib/postgresql/9.4/bin/postgres -D /var/lib/postgresql/9.4/main -c config_file=/etc/postgresql/9.4/main/postgresql.conf'),
('df4464a3-4527-4717-8106-1e2f608e4d9e', -3769833592930856651, '4f3e863b-72bc-4993-83f6-0242bbdbb467', TIMESTAMP '2019-04-15 15:12:50.764634', NULL, 0, '0', 167804870, '10.0.127.198', '59ed5983-da6a-4036-b2e9-9c613594ddb9', -2038388685, '134.128.168.51', '00000000-0000-0000-0000-000000000000', 59360, 443, 'harper', 1001, 846883, 3313, 'chrome', '/opt/google/chrome/chrome');

INSERT INTO PUBLIC.CONNECTION(ID, CONNECTION_HASH, AGENT_ID, CONNECTION_STARTED, CONNECTION_ENDED, DURATION, PROTOCOL, SOURCE_ADDRESS, SOURCE_ADDRESS_STRING, SOURCE_NETWORK_ID, DESTINATION_ADDRESS, DESTINATION_ADDRESS_STRING, DESTINATION_NETWORK_ID, SOURCE_PORT, DESTINATION_PORT, USERNAME, UID, INODE, PID, PROCESS_NAME, COMMAND_LINE) VALUES
('716119f3-eab9-409f-b359-520a62ba2ef8', -977270226615511806, '0aef9c8d-18f4-4623-b271-d576fe0467a6', TIMESTAMP '2019-04-15 15:12:51.77538', NULL, 0, '0', 167782697, '10.0.41.41', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 167835007, '10.0.245.127', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 22772, 5432, 'harper', 1001, 762091, 8781, 'psql', '/usr/lib/postgresql/9.4/bin/psql -h 10.0.245.127'),
('b9279ff9-7638-4b70-9c7c-9834707a6f81', -977270226615511806, 'e282a292-a759-46b9-a0fe-5f351cffa5f6', TIMESTAMP '2019-04-15 15:12:51.77538', NULL, 0, '0', 167782697, '10.0.41.41', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 167835007, '10.0.245.127', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 22772, 5432, 'postgres', 108, 982447, 6618, 'postgres', '/usr/lib/postgresql/9.4/bin/postgres -D /var/lib/postgresql/9.4/main -c config_file=/etc/postgresql/9.4/main/postgresql.conf'),
('e34ea62e-7d47-43c4-833f-79d91e3d16ab', -787488501349225192, 'f46c1bbf-71f5-4c23-a211-c0a2dacf8c24', TIMESTAMP '2019-04-15 15:12:52.841513', NULL, 0, '0', 167818874, '10.0.182.122', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 167832238, '10.0.234.174', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 34329, 22, 'freya', 1001, 734172, 8098, 'ssh', '/usr/bin/ssh 10.0.234.174'),
('efb916bb-cdfc-4b38-9815-e264ed399c53', -787488501349225192, '713b49f6-a676-4e1b-bec7-aba2a0010b08', TIMESTAMP '2019-04-15 15:12:52.841513', NULL, 0, '0', 167818874, '10.0.182.122', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 167832238, '10.0.234.174', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 34329, 22, 'root', 0, 806556, 7811, 'sshd', '/usr/sbin/sshd [accepted]'),
('fd7d566c-4e97-4207-890b-480c5355a912', 2390210494172868005, 'afdbbc76-5dc9-452c-a248-45cb5aa0f769', TIMESTAMP '2019-04-15 15:12:53.887328', NULL, 0, '0', 167798336, '10.0.102.64', '59ed5983-da6a-4036-b2e9-9c613594ddb9', -419506885, '230.254.213.59', '00000000-0000-0000-0000-000000000000', 35969, 443, 'evelyn', 1001, 914523, 4336, 'chrome', '/opt/google/chrome/chrome'),
('9ed8ecd4-a34d-49d2-8c61-e44fcdc7c713', -112018405224810307, 'e282a292-a759-46b9-a0fe-5f351cffa5f6', TIMESTAMP '2019-04-15 15:12:54.913625', NULL, 0, '0', 167835007, '10.0.245.127', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 167828703, '10.0.220.223', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 24063, 22, 'harper', 1001, 841404, 5114, 'ssh', '/usr/bin/ssh 10.0.220.223'),
('749b2f02-2aeb-42d9-8cdf-eecb7b0d7f06', -112018405224810307, 'e6d8bf5c-a70d-46d7-8e50-3f9cfc30938e', TIMESTAMP '2019-04-15 15:12:54.913625', NULL, 0, '0', 167835007, '10.0.245.127', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 167828703, '10.0.220.223', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 24063, 22, 'root', 0, 767143, 6785, 'sshd', '/usr/sbin/sshd [accepted]');

INSERT INTO PUBLIC.CONNECTION_LINK(ID, CONNECTION_HASH, CREATED_ON, ENDED_ON, DURATION, SOURCE_AGENT_ID, SOURCE_NETWORK_ID, DESTINATION_NETWORK_ID, DESTINATION_AGENT_ID, SOURCE_CONNECTION_ID, SOURCE_ADDRESS, SOURCE_ADDRESS_STRING, SOURCE_PORT, DESTINATION_ADDRESS, DESTINATION_ADDRESS_STRING, DESTINATION_PORT, ALIVE, DESTINATION_CONNECTION_ID, SOURCE_PROCESS_NAME, DESTINATION_PROCESS_NAME, SOURCE_USERNAME, DESTINATION_USERNAME) VALUES
('f9510bdc-2467-4925-af90-bd90de179086', 6154602466870092764, TIMESTAMP '2019-04-15 15:12:26.744211', TIMESTAMP '2019-04-15 15:12:30.744211', 4, '0aef9c8d-18f4-4623-b271-d576fe0467a6', '59ed5983-da6a-4036-b2e9-9c613594ddb9', '59ed5983-da6a-4036-b2e9-9c613594ddb9', '83fe17cc-2017-4f51-a370-9b8cca550025', '6ae47a0c-94f8-44c4-9074-79aca8ce79ab', 167782697, '10.0.41.41', 12937, 167835471, '10.0.247.79', 5432, FALSE, '13e0f7bf-40a7-4c49-a711-80f8e92f994d', 'psql', 'postgres', 'mia', 'postgres'),
('e9245e97-bad1-464a-908c-cdcb78e9108b', 2417520910791374621, TIMESTAMP '2019-04-15 15:12:27.857681', NULL, 0, '0aef9c8d-18f4-4623-b271-d576fe0467a6', '59ed5983-da6a-4036-b2e9-9c613594ddb9', '59ed5983-da6a-4036-b2e9-9c613594ddb9', '4f3e863b-72bc-4993-83f6-0242bbdbb467', '42c5e1f9-25cb-425e-8c31-d7c8c0b72fd8', 167782697, '10.0.41.41', 53546, 167804870, '10.0.127.198', 22, TRUE, '89996c58-102c-4076-a4ac-ead8506c05a6', 'ssh', 'sshd', 'jessica', 'root'),
('b8dcd4eb-0d96-4837-b3fd-be53335e9d0d', 5148955430400096458, TIMESTAMP '2019-04-15 15:12:28.935277', NULL, 0, 'e282a292-a759-46b9-a0fe-5f351cffa5f6', '59ed5983-da6a-4036-b2e9-9c613594ddb9', '00000000-0000-0000-0000-000000000000', NULL, 'b913fcfc-ddf4-4188-bcb9-1dea75770a62', 167835007, '10.0.245.127', 41997, 122135634, '7.71.164.82', 443, TRUE, NULL, 'chrome', NULL, 'daisy', NULL),
('8d11959b-1128-40d0-bf2b-c67004d09779', 4454544584831484664, TIMESTAMP '2019-04-15 15:12:29.958112', NULL, 0, 'f46c1bbf-71f5-4c23-a211-c0a2dacf8c24', '59ed5983-da6a-4036-b2e9-9c613594ddb9', '59ed5983-da6a-4036-b2e9-9c613594ddb9', '1042530b-5d04-45ca-b225-d50e9f3503ab', '3038d988-32d7-4c00-b2d4-5e35879d97a9', 167818874, '10.0.182.122', 50042, 167813943, '10.0.163.55', 5432, TRUE, 'e6e1f0b7-c591-42c2-bdc4-5c5dcb58d60c', 'psql', 'postgres', 'harper', 'postgres'),
('f52fde7b-4c14-419b-bf0c-12cc8c1e6022', 2348118243233934881, TIMESTAMP '2019-04-15 15:12:30.992693', NULL, 0, 'e282a292-a759-46b9-a0fe-5f351cffa5f6', '59ed5983-da6a-4036-b2e9-9c613594ddb9', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 'afdbbc76-5dc9-452c-a248-45cb5aa0f769', '1cfd0454-6e33-4f2d-8b21-a03346a24e8c', 167835007, '10.0.245.127', 29371, 167798336, '10.0.102.64', 22, TRUE, '485330bb-91c3-4100-af6d-0ae3d2a396ea', 'ssh', 'sshd', 'harper', 'root'),
('8cc189c6-f0c2-464b-8bb4-066cf156095c', 2053105620372032212, TIMESTAMP '2019-04-15 15:12:32.087332', TIMESTAMP '2019-04-15 15:12:36.087332', 4, 'b2258d50-68bd-4f97-9e17-af128be6a9bb', '59ed5983-da6a-4036-b2e9-9c613594ddb9', '00000000-0000-0000-0000-000000000000', NULL, 'ee1c9cb5-e76e-4a87-a3aa-ae2654517d80', 167804391, '10.0.125.231', 38177, -1792940002, '149.33.232.30', 443, FALSE, NULL, 'chrome', NULL, 'sienna', NULL),
('c8de339b-e648-45f0-b560-e661bc065027', -2493897573505804936, TIMESTAMP '2019-04-15 15:12:33.123698', NULL, 0, 'f46c1bbf-71f5-4c23-a211-c0a2dacf8c24', '59ed5983-da6a-4036-b2e9-9c613594ddb9', '59ed5983-da6a-4036-b2e9-9c613594ddb9', '4f3e863b-72bc-4993-83f6-0242bbdbb467', 'aaf5d9fa-ea80-4b3a-bd71-9f90fe036246', 167818874, '10.0.182.122', 38501, 167804870, '10.0.127.198', 22, TRUE, '7bc340a5-efd1-4901-8f2b-050dc15fb851', 'ssh', 'sshd', 'freya', 'root'),
('afce40f6-9f95-4a99-82eb-4af1da2e9fa6', 6016087765958840248, TIMESTAMP '2019-04-15 15:12:34.190913', NULL, 0, 'b2258d50-68bd-4f97-9e17-af128be6a9bb', '59ed5983-da6a-4036-b2e9-9c613594ddb9', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 'e282a292-a759-46b9-a0fe-5f351cffa5f6', 'cc8cc572-096c-4ea4-9106-948dd44e586a', 167804391, '10.0.125.231', 7385, 167835007, '10.0.245.127', 5432, TRUE, 'd76d0a6a-1194-4cd6-b1ec-7c4bd863c57b', 'psql', 'postgres', 'poppy', 'postgres'),
('f6363d0d-124c-4a8a-8560-c4e0cf7b0b99', 4332363873623632806, TIMESTAMP '2019-04-15 15:12:35.223274', NULL, 0, '1042530b-5d04-45ca-b225-d50e9f3503ab', '59ed5983-da6a-4036-b2e9-9c613594ddb9', '00000000-0000-0000-0000-000000000000', NULL, '480220aa-4556-49db-bd0f-529c7ada4cf0', 167813943, '10.0.163.55', 3517, 2004143314, '119.116.204.210', 443, TRUE, NULL, 'chrome', NULL, 'olivia', NULL);

INSERT INTO PUBLIC.CONNECTION_LINK(ID, CONNECTION_HASH, CREATED_ON, ENDED_ON, DURATION, SOURCE_AGENT_ID, SOURCE_NETWORK_ID, DESTINATION_NETWORK_ID, DESTINATION_AGENT_ID, SOURCE_CONNECTION_ID, SOURCE_ADDRESS, SOURCE_ADDRESS_STRING, SOURCE_PORT, DESTINATION_ADDRESS, DESTINATION_ADDRESS_STRING, DESTINATION_PORT, ALIVE, DESTINATION_CONNECTION_ID, SOURCE_PROCESS_NAME, DESTINATION_PROCESS_NAME, SOURCE_USERNAME, DESTINATION_USERNAME) VALUES
('f70006c8-aa7e-43bb-a9f7-a16ff1e216f1', -7045999014692899530, TIMESTAMP '2019-04-15 15:12:36.242045', NULL, 0, 'afdbbc76-5dc9-452c-a248-45cb5aa0f769', '59ed5983-da6a-4036-b2e9-9c613594ddb9', '59ed5983-da6a-4036-b2e9-9c613594ddb9', '0aef9c8d-18f4-4623-b271-d576fe0467a6', 'fa06b7ef-f7cc-4710-9421-a92c8cf37b21', 167798336, '10.0.102.64', 39871, 167782697, '10.0.41.41', 22, TRUE, '3f2d9683-cc63-4816-8005-572ca893b367', 'ssh', 'sshd', 'amelia', 'root'),
('92ac752d-5ddc-4107-b25f-394b0e5630a9', 7617953247103136494, TIMESTAMP '2019-04-15 15:12:37.324762', NULL, 0, '4f3e863b-72bc-4993-83f6-0242bbdbb467', '59ed5983-da6a-4036-b2e9-9c613594ddb9', '59ed5983-da6a-4036-b2e9-9c613594ddb9', '83fe17cc-2017-4f51-a370-9b8cca550025', 'dbddf3ba-41cb-4654-8b48-0b0a445ed445', 167804870, '10.0.127.198', 59520, 167835471, '10.0.247.79', 5432, TRUE, '9f3d0633-a386-4b1d-93dc-fcd2d0446763', 'psql', 'postgres', 'jessica', 'postgres'),
('80c82fdd-0be9-48fa-9af2-ccfd3b2bd713', -8906163645830194496, TIMESTAMP '2019-04-15 15:12:38.355214', NULL, 0, 'afdbbc76-5dc9-452c-a248-45cb5aa0f769', '59ed5983-da6a-4036-b2e9-9c613594ddb9', '00000000-0000-0000-0000-000000000000', NULL, '2adf6333-d91a-475e-96b9-0b0a1131b39c', 167798336, '10.0.102.64', 31223, 741831390, '44.55.114.222', 443, TRUE, NULL, 'chrome', NULL, 'freya', NULL),
('b09a1965-b962-4117-ab87-5e039cd28ad2', 3032480449985072106, TIMESTAMP '2019-04-15 15:12:39.393861', NULL, 0, 'e282a292-a759-46b9-a0fe-5f351cffa5f6', '59ed5983-da6a-4036-b2e9-9c613594ddb9', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 'afdbbc76-5dc9-452c-a248-45cb5aa0f769', '1c597fb7-d4c8-40d6-b371-b0b3ef591bc5', 167835007, '10.0.245.127', 17142, 167798336, '10.0.102.64', 22, TRUE, 'b7e04802-97cd-4bd5-80df-a8b33cf904ec', 'ssh', 'sshd', 'ella', 'root'),
('7580acd1-0b89-45fb-be41-f5e982c688db', -3425257438626106169, TIMESTAMP '2019-04-15 15:12:40.422363', NULL, 0, 'e282a292-a759-46b9-a0fe-5f351cffa5f6', '59ed5983-da6a-4036-b2e9-9c613594ddb9', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 'b2258d50-68bd-4f97-9e17-af128be6a9bb', 'd0c9f707-2ebb-4530-8fae-beb97294a5fa', 167835007, '10.0.245.127', 51591, 167804391, '10.0.125.231', 5432, TRUE, '3e03f111-ae5a-45f2-81d6-a1d9fb837919', 'psql', 'postgres', 'ivy', 'postgres'),
('51aa2ef1-7432-4500-9881-f31c5535bd33', 2229469083177408084, TIMESTAMP '2019-04-15 15:12:41.451405', NULL, 0, '713b49f6-a676-4e1b-bec7-aba2a0010b08', '59ed5983-da6a-4036-b2e9-9c613594ddb9', '00000000-0000-0000-0000-000000000000', NULL, '82dd9b6e-aa14-47da-ad8d-6952217c69d9', 167832238, '10.0.234.174', 34420, -626611614, '218.166.170.98', 443, TRUE, NULL, 'chrome', NULL, 'mia', NULL),
('eee3ac11-6cd6-4a16-8ffe-46d153ec2f41', -2605722118715978430, TIMESTAMP '2019-04-15 15:12:42.477487', NULL, 0, 'afdbbc76-5dc9-452c-a248-45cb5aa0f769', '59ed5983-da6a-4036-b2e9-9c613594ddb9', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 'e6d8bf5c-a70d-46d7-8e50-3f9cfc30938e', 'd93bc47d-39b4-4684-bec8-0dbdddfb624f', 167798336, '10.0.102.64', 49002, 167828703, '10.0.220.223', 22, TRUE, '656668bc-761c-49f8-8967-cd2ba5ec01f9', 'ssh', 'sshd', 'poppy', 'root'),
('bb260bbc-5f49-40d7-b971-5e6e4e910049', 233254759164713446, TIMESTAMP '2019-04-15 15:12:43.506692', NULL, 0, '0aef9c8d-18f4-4623-b271-d576fe0467a6', '59ed5983-da6a-4036-b2e9-9c613594ddb9', '59ed5983-da6a-4036-b2e9-9c613594ddb9', '713b49f6-a676-4e1b-bec7-aba2a0010b08', '29ae7f33-7564-4830-bce5-88a9d9b64c7f', 167782697, '10.0.41.41', 41842, 167832238, '10.0.234.174', 22, TRUE, '0120fe57-23a6-40b1-8983-caad3115044b', 'ssh', 'sshd', 'isabelle', 'root'),
('57fa632d-9ae2-42bc-bb5b-0f3a5c150d96', 5072807480960586777, TIMESTAMP '2019-04-15 15:12:44.533924', NULL, 0, '0aef9c8d-18f4-4623-b271-d576fe0467a6', '59ed5983-da6a-4036-b2e9-9c613594ddb9', '00000000-0000-0000-0000-000000000000', NULL, '1e980741-74b9-487b-86dd-17fb3ae70646', 167782697, '10.0.41.41', 48005, -1328111221, '176.214.161.139', 443, TRUE, NULL, 'chrome', NULL, 'isabella', NULL);

INSERT INTO PUBLIC.CONNECTION_LINK(ID, CONNECTION_HASH, CREATED_ON, ENDED_ON, DURATION, SOURCE_AGENT_ID, SOURCE_NETWORK_ID, DESTINATION_NETWORK_ID, DESTINATION_AGENT_ID, SOURCE_CONNECTION_ID, SOURCE_ADDRESS, SOURCE_ADDRESS_STRING, SOURCE_PORT, DESTINATION_ADDRESS, DESTINATION_ADDRESS_STRING, DESTINATION_PORT, ALIVE, DESTINATION_CONNECTION_ID, SOURCE_PROCESS_NAME, DESTINATION_PROCESS_NAME, SOURCE_USERNAME, DESTINATION_USERNAME) VALUES
('2bb419e9-180d-4d14-bd48-cdf02a15a766', -8328445360121123683, TIMESTAMP '2019-04-15 15:12:45.572996', TIMESTAMP '2019-04-15 15:12:45.572996', 0, 'e282a292-a759-46b9-a0fe-5f351cffa5f6', '59ed5983-da6a-4036-b2e9-9c613594ddb9', '59ed5983-da6a-4036-b2e9-9c613594ddb9', '1042530b-5d04-45ca-b225-d50e9f3503ab', 'df71ac09-950e-4063-873b-ecb784c316a7', 167835007, '10.0.245.127', 8190, 167813943, '10.0.163.55', 22, FALSE, 'a1259bcb-94ff-4ef5-b89c-71ef1f079cf5', 'ssh', 'sshd', 'chloe', 'root'),
('bc846bce-fe23-4ee6-9c41-a6ea00d7946c', 4198137929323018877, TIMESTAMP '2019-04-15 15:12:46.666848', TIMESTAMP '2019-04-15 15:12:54.666848', 8, '713b49f6-a676-4e1b-bec7-aba2a0010b08', '59ed5983-da6a-4036-b2e9-9c613594ddb9', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 'afdbbc76-5dc9-452c-a248-45cb5aa0f769', 'b54c8f4f-cb4e-409b-9899-af080ea4daf2', 167832238, '10.0.234.174', 56468, 167798336, '10.0.102.64', 22, FALSE, '9a5d7bd7-39be-4888-a85b-dce04c5c899e', 'ssh', 'sshd', 'scarlett', 'root'),
('1d477147-e5d3-4376-b321-826f25d128cf', 937696346245267234, TIMESTAMP '2019-04-15 15:12:47.692844', NULL, 0, '0aef9c8d-18f4-4623-b271-d576fe0467a6', '59ed5983-da6a-4036-b2e9-9c613594ddb9', '00000000-0000-0000-0000-000000000000', NULL, 'ddb73e08-21ab-43e8-aedb-7ccab4cdea2b', 167782697, '10.0.41.41', 32385, -1032135777, '194.122.219.159', 443, TRUE, NULL, 'chrome', NULL, 'jessica', NULL),
('6a9f1438-2476-49d1-93b1-f57e51a83da2', 9131331755688410496, TIMESTAMP '2019-04-15 15:12:48.705835', NULL, 0, 'b2258d50-68bd-4f97-9e17-af128be6a9bb', '59ed5983-da6a-4036-b2e9-9c613594ddb9', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 'f46c1bbf-71f5-4c23-a211-c0a2dacf8c24', 'adf1f20a-667d-4352-ae1b-5d2fdacaf2a5', 167804391, '10.0.125.231', 37369, 167818874, '10.0.182.122', 22, TRUE, '5e7cd734-b119-4df0-b053-93bee419a4d1', 'ssh', 'sshd', 'evie', 'root'),
('bb6bd484-ab51-450a-a3b3-897fff4ba38b', 1201596231777073575, TIMESTAMP '2019-04-15 15:12:49.733497', NULL, 0, 'e6d8bf5c-a70d-46d7-8e50-3f9cfc30938e', '59ed5983-da6a-4036-b2e9-9c613594ddb9', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 'afdbbc76-5dc9-452c-a248-45cb5aa0f769', '95674412-9e7c-4d5c-b17e-8c7d97b609ed', 167828703, '10.0.220.223', 3879, 167798336, '10.0.102.64', 5432, TRUE, 'aaf3147b-5f82-4036-9a32-89ab16047760', 'psql', 'postgres', 'amelia', 'postgres'),
('66cce356-4085-4016-8c49-8cd89a73d22d', -3769833592930856651, TIMESTAMP '2019-04-15 15:12:50.764634', NULL, 0, '4f3e863b-72bc-4993-83f6-0242bbdbb467', '59ed5983-da6a-4036-b2e9-9c613594ddb9', '00000000-0000-0000-0000-000000000000', NULL, 'df4464a3-4527-4717-8106-1e2f608e4d9e', 167804870, '10.0.127.198', 59360, -2038388685, '134.128.168.51', 443, TRUE, NULL, 'chrome', NULL, 'harper', NULL),
('5ab1a8ea-7d4b-4325-bc5c-85a339920920', -977270226615511806, TIMESTAMP '2019-04-15 15:12:51.77538', NULL, 0, '0aef9c8d-18f4-4623-b271-d576fe0467a6', '59ed5983-da6a-4036-b2e9-9c613594ddb9', '59ed5983-da6a-4036-b2e9-9c613594ddb9', 'e282a292-a759-46b9-a0fe-5f351cffa5f6', '716119f3-eab9-409f-b359-520a62ba2ef8', 167782697, '10.0.41.41', 22772, 167835007, '10.0.245.127', 5432, TRUE, 'b9279ff9-7638-4b70-9c7c-9834707a6f81', 'psql', 'postgres', 'harper', 'postgres'),
('a4ac1d2f-1069-4253-bf44-814566cb8aef', -787488501349225192, TIMESTAMP '2019-04-15 15:12:52.841513', NULL, 0, 'f46c1bbf-71f5-4c23-a211-c0a2dacf8c24', '59ed5983-da6a-4036-b2e9-9c613594ddb9', '59ed5983-da6a-4036-b2e9-9c613594ddb9', '713b49f6-a676-4e1b-bec7-aba2a0010b08', 'e34ea62e-7d47-43c4-833f-79d91e3d16ab', 167818874, '10.0.182.122', 34329, 167832238, '10.0.234.174', 22, TRUE, 'efb916bb-cdfc-4b38-9815-e264ed399c53', 'ssh', 'sshd', 'freya', 'root'),
('556f01cd-48fe-48ae-b6c2-e3598af5a9b9', 2390210494172868005, TIMESTAMP '2019-04-15 15:12:53.887328', NULL, 0, 'afdbbc76-5dc9-452c-a248-45cb5aa0f769', '59ed5983-da6a-4036-b2e9-9c613594ddb9', '00000000-0000-0000-0000-000000000000', NULL, 'fd7d566c-4e97-4207-890b-480c5355a912', 167798336, '10.0.102.64', 35969, -419506885, '230.254.213.59', 443, TRUE, NULL, 'chrome', NULL, 'evelyn', NULL);


