import socket

# Set the IP address and Port of the CyBot server
HOST = "192.168.1.1"  # IP address of the CyBot server
PORT = 288            # Port number for CyBot server

# Create a TCP socket and connect to the CyBot server
try:
    cybot_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    cybot_socket.connect((HOST, PORT))

    # Instructions for the user
    print("Press 'm' to start main.c on the CyBot. Enter 'quit' to exit.")

    while True:
        command = input("Enter a command: ")  # Get the command from the user
        if command == 'm':
            cybot_socket.sendall(command.encode())  # Send command to CyBot
            print("Command 'm' sent to start main.c")
        elif command == 'quit':
            print("Exiting program.")
            break
        else:
            print("Invalid command. Only 'm' is accepted to start main.c.")

    # Close the socket connection
    cybot_socket.close()
except Exception as e:
    print(f"Connection failed: {e}")
