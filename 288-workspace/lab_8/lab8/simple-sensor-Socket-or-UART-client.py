import socket

# Set the IP address and port of the CyBot server
HOST = "192.168.1.1"  # Replace with your CyBot's IP address
PORT = 288  # Replace with the appropriate port

# Create a TCP socket and connect to the CyBot server
cybot_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
cybot_socket.connect((HOST, PORT))

# Instructions for controlling the CyBot
print("Use 'w' to move forward, 's' to move backward, 'a' to turn left, 'd' to turn right.")
print("Use 't' to toggle modes (Manual/Autonomous), 'h' to start scanning/moving in autonomous mode.")
print("Type 'quit' to exit.")

while True:
    command = input("Enter a command: ")  # Get movement command from the user

    # Send the command to the CyBot
    if command in ['w', 'a', 's', 'd', 't', 'h']:
        cybot_socket.sendall(command.encode())  # Send command to the CyBot

        # Receive and display the response from the CyBot
        data = cybot_socket.recv(1024).decode()
        if data:
            print(f"Received from CyBot: {data}")

    elif command == 'quit':
        cybot_socket.sendall(command.encode())  # Send quit command to CyBot
        break

    else:
        print("Invalid command. Use 'w', 'a', 's', 'd', 't', 'h', or 'quit'.")

# Close the socket connection
cybot_socket.close()
print("Connection closed.")




