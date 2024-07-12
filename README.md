# Codex Naturalis

## Software Engineering Project - Politecnico di Milano
Members:
* Kevin Ziroldi
* Matteo Volpari
* Samuele Villa
* Tommaso Uberti Foppa

## Implemented features
| Feature                      | Implemented |
|------------------------------|-------------|
| Simplified rules             | ✅           |
| Complete rules               | ✅           |
| Socket                       | ✅           |
| RMI                          | ✅           |
| TUI                          | ✅           |
| GUI                          | ✅           |
| Multiple games               | ✅           |
| Persistence                  | ❌           | 
| Resilience to disconnections | ✅           |
| Chat                         | ✅           |

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

## Disclaimer
NOTA: Codex Naturalis è un gioco da tavolo sviluppato ed edito da Cranio Creations Srl. I contenuti grafici di questo progetto riconducibili al prodotto editoriale da tavolo sono utilizzati previa approvazione di Cranio Creations Srl a solo scopo didattico. È vietata la distribuzione, la copia o la riproduzione dei contenuti e immagini in qualsiasi forma al di fuori del progetto, così come la redistribuzione e la pubblicazione dei contenuti e immagini a fini diversi da quello sopracitato. È inoltre vietato l'utilizzo commerciale di suddetti contenuti.
