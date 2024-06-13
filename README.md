# Codex Naturalis
# Software Engineering Project - Politecnico di Milano

## Implemented features
| Feature                        | Implemented |
|--------------------------------|-------------|
| Regole semplificate            | ✅           |
| Regole complete                | ✅           |
| Socket                         | ✅           |
| RMI                            | ✅           |
| TUI                            | ✅           |
| GUI                            | ✅           |
| Partite Multiple               | ✅           |
| Persistenza                    | ❌           | 
| Resilienza alle disconnessioni | ✅           |
| Chat                           | ✅           |

## How to install and run the project
In the deliverables folder, you can find two jar files:
 * `CodexNaturalis-Server.jar`
 * `CodexNaturalis-Client.jar` 

### How to start the server 
In order to start the server, follow these steps:
1. Download the server jar; 
2. Open terminal and move to the directory containing the file;
3. Type `java -jar CodexNaturalis-Server.jar <server_ip> <rmi_server_port> <socket_server_port>`.

### How to start the client
In order to start the client, follow these steps:
1. Download the client jar;
2. Open terminal and move to the directory containing the file;
3. Type `java -jar CodexNaturalis-Client.jar <rmi_server_port> <socket_server_port>`.