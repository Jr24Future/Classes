import tkinter as tk
import socket

# Set the IP address and Port of the CyBot server
HOST = "192.168.1.1"  # IP address of the CyBot server
PORT = 288            # Port number for CyBot server

# Function to send a command to the CyBot server
def send_command(command):
    try:
        # Create a TCP socket and connect to the CyBot server
        cybot_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        cybot_socket.connect((HOST, PORT))

        # Send the command to the CyBot
        cybot_socket.sendall(command.encode())
        
        # Display feedback to the user
        feedback_label.config(text=f"Command '{command}' sent")
        
        # Close the connection after sending the command
        cybot_socket.close()
    except Exception as e:
        feedback_label.config(text=f"Connection failed: {e}")

# Create the main application window
root = tk.Tk()
root.title("CyBot Control Interface")

# Create a large button to send the 'm' command
start_button = tk.Button(root, text="Start CyBot", font=("Helvetica", 24), command=lambda: send_command('m'))
start_button.pack(pady=(50, 10), padx=50)

# Create a smaller button to send the 's' command to stop
stop_button = tk.Button(root, text="Stop CyBot", font=("Helvetica", 16), command=lambda: send_command('s'))
stop_button.pack(pady=10, padx=50)

# Label to display feedback
feedback_label = tk.Label(root, text="", font=("Helvetica", 16))
feedback_label.pack(pady=20)

# Run the Tkinter event loop
root.mainloop()
