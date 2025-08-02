import tkinter as tk
import socket
import matplotlib.pyplot as plt

# Set the IP address and Port of the CyBot server
HOST = "192.168.1.1"  # IP address of the CyBot server
PORT = 288            # Port number for CyBot server

# Function to send a command to the CyBot server and receive data
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

        # If the command is 'h' (scan), show feedback and allow graph display
        if command == 'h':
            feedback_label.config(text="Scan completed. Press 'Display Graph' to view results.")
    except Exception as e:
        feedback_label.config(text=f"Connection failed: {e}")

# Function to display a graph of the scan data from putty.txt
def display_graph():
    angles = []
    distances = []

    # Read the data from putty.txt
    try:
        with open("putty.txt", "r") as file:
            for line in file:
                if "Angle:" in line and "IR Distance:" in line:
                    parts = line.split()
                    angle = int(parts[1])
                    distance = float(parts[-2])  # Extracting the numeric value before 'cm'
                    angles.append(angle)
                    distances.append(distance)

        # Plot the data using matplotlib
        if angles and distances:
            plt.figure(figsize=(10, 5))
            plt.plot(angles, distances, marker='o', linestyle='-', color='b')
            plt.title('IR Distance vs Angle')
            plt.xlabel('Angle (Degrees)')
            plt.ylabel('IR Distance (cm)')
            plt.grid(True)
            plt.show()
        else:
            feedback_label.config(text="No valid data found in putty.txt")
    except FileNotFoundError:
        feedback_label.config(text="putty.txt not found. Ensure the file is present.")

# Create the main application window
root = tk.Tk()
root.title("CyBot Control Interface")

# Define frame for movement controls
movement_frame = tk.Frame(root)
movement_frame.pack(pady=20)

# Arrow buttons arranged in a diamond layout
button_up = tk.Button(movement_frame, text="^", font=("Helvetica", 20), command=lambda: send_command('w'))
button_up.grid(row=0, column=1)

button_left = tk.Button(movement_frame, text="<", font=("Helvetica", 20), command=lambda: send_command('a'))
button_left.grid(row=1, column=0)

button_right = tk.Button(movement_frame, text=">", font=("Helvetica", 20), command=lambda: send_command('d'))
button_right.grid(row=1, column=2)

button_down = tk.Button(movement_frame, text="v", font=("Helvetica", 20), command=lambda: send_command('s'))
button_down.grid(row=2, column=1)

# Frame to hold mode toggle and scan buttons
mode_frame = tk.Frame(root)
mode_frame.pack(pady=20)

# Function to switch between manual and scan mode
def toggle_mode():
    global current_mode
    send_command('t')  # Send the 't' command to switch the mode on the CyBot
    if current_mode == "Manual":
        current_mode = "Scan"
        feedback_label.config(text="Switched to Scan Mode")
        mode_button.config(text="Switch to Manual Mode")
        movement_frame.pack_forget()
        scan_button.pack(pady=20)
        display_button.pack(pady=10)
    else:
        current_mode = "Manual"
        feedback_label.config(text="Switched to Manual Mode")
        mode_button.config(text="Switch to Scan Mode")
        movement_frame.pack(pady=20)
        scan_button.pack_forget()
        display_button.pack_forget()

current_mode = "Manual"

# Button to toggle between modes
mode_button = tk.Button(mode_frame, text="Switch to Scan Mode", font=("Helvetica", 16), command=toggle_mode)
mode_button.pack()

# Scan button (initially hidden)
scan_button = tk.Button(root, text="Scan", font=("Helvetica", 20), command=lambda: send_command('h'))
scan_button.pack_forget()

# Button to display the graph after scan completion
display_button = tk.Button(root, text="Display Graph", font=("Helvetica", 20), command=display_graph)
display_button.pack_forget()

# Label to display feedback
feedback_label = tk.Label(root, text="Manual Mode Active", font=("Helvetica", 16))
feedback_label.pack(pady=10)

# Run the Tkinter event loop
root.mainloop()
