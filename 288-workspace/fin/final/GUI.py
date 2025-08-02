import time # Time library   
# Socket library:  https://realpython.com/python-sockets/  
# See: Background, Socket API Overview, and TCP Sockets  
import socket   
import matplotlib.pyplot as plt
import numpy as np
import tkinter as tk
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg

# Choose to create either a UART or TCP port socket to communicate with Cybot (Not both!!: I.e, comment out the one not being used)
# UART BEGIN
#cybot = serial.Serial('COM100', 115200)  # UART (Make sure you are using the correct COM port and Baud rate!!)
# UART END

# TCP Socket BEGIN (See Echo Client example): https://realpython.com/python-sockets/#echo-client-and-server
HOST = "192.168.1.1"  # The server's hostname or IP address
PORT = 288        # The port used by the server
cybot_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)  # Create a socket object
cybot_socket.connect((HOST, PORT))   # Connect to the socket  (Note: Server must first be running)
                      
cybot = cybot_socket.makefile("rbw", buffering=0)  # makefile creates a file object out of a socket:  https://pythontic.com/modules/socket/makefile
# TCP Socket END

# Send some text: Either 1) Choose "Hello" or 2) have the user enter text to send
#send_message = input()                            # 1) Hard code message to "Hello", or
#send_message = input("Enter a message:") + '\n'   # 2) Have user enter text

#cybot.write(send_message.encode()) # Convert String to bytes (i.e., encode), and send data to the server
toggle = 0

def scan():
        global toggle
        send_message = 'h\n'
        cybot.write(send_message.encode())
        angle_degrees = []
        angle_radians = []
        distance = []
        distanceIR = []
        message = []
        rx_str = '0'
        rx_message = 0
        #print("wait for server reply\n")
        while rx_str != '```\n':
                rx_message = cybot.readline()      # Wait for a message, readline expects message to end with "\n"
                rx_str = rx_message.decode()
                if rx_str != '```\n':
                        message.append(rx_str)
                        #print( rx_str ) # Convert message from bytes to String (i.e., decode)
                else:
                        break
        if send_message == 'h\n':
                label_msg = message[1:]
                message = message[2:93]
                print(message)
        label.config(text = "".join(label_msg))

        #print(message)             
        for i in message:
                data = i.split()

                #print(str(data[0]) + ' ' + str(data[1]) + ' ' + str(data[2]))
                angle_degrees.append(float(data[0]))  # Column 0 holds the angle at which distance was measured
                distance.append(float(data[1]))       # Column 1 holds the distance that was measured at a given angle       
                distanceIR.append(float(data[2]))
        angle_degrees = np.array(angle_degrees)
        angle_radians = (np.pi/180) * angle_degrees
        
        fig, ax = plt.subplots(subplot_kw={'projection': 'polar'}) # One subplot of type polar
        ax.plot(angle_radians, distanceIR, color='r', linewidth=4.0)  # Plot distance verse angle (in radians), using red, line width 4.0
        ax.set_xlabel('IR Distance (m)', fontsize = 14.0)  # Label x axis
        ax.set_ylabel('Angle (degrees)', fontsize = 14.0) # Label y axis
        ax.xaxis.set_label_coords(0.5, 0.15) # Modify location of x axis label (Typically do not need or want this)
        ax.tick_params(axis='both', which='major', labelsize=14) # set font size of tick labels
        ax.set_rmax(.5)                    # Saturate distance at 2.5 meters
        ax.set_rticks([0.1, .2, .3, .4])   # Set plot "distance" tick marks at .5, 1, 1.5, 2, and 2.5 meters
        ax.set_rlabel_position(-22.5)     # Adjust location of the radial labels
        ax.set_thetamax(180)              # Saturate angle to 180 degrees
        ax.set_xticks(np.arange(0,np.pi+.1,np.pi/4)) # Set plot "angle" tick marks to pi/4 radians (i.e., displayed at 45 degree) increments
                                             # Note: added .1 to pi to go just beyond pi (i.e., 180 degrees) so that 180 degrees is displayed
        ax.grid(True)                     # Show grid lines

        # Create title for plot (font size = 14pt, y & pad controls title vertical location)
        ax.set_title("Object detection 0 - 180 degrees", size=14, y=1.0, pad=-24)
        for i in canvas_widgets:
                i.get_tk_widget().destroy()
        canvas2 = FigureCanvasTkAgg(fig, master = window)
        canvas2.draw()
        canvas2.get_tk_widget().pack(side = 'right', anchor = 'se', padx=10, pady=10)
        canvas_widgets.append(canvas2)
        #plt.show()  # Display plot

def forward():
        send_message = 'w\n'
        cybot.write(send_message.encode())

def left():
        send_message = 'a\n'
        cybot.write(send_message.encode())

def right():
        send_message = 'd\n'
        cybot.write(send_message.encode())

def backward():
        send_message = 's\n'
        cybot.write(send_message.encode())

def toggleMode():
        global toggle
        if toggle == 0:
                toggle = 1
        else:
                toggle = 0
        send_message = 't\n'
        cybot.write(send_message.encode())
        message = []
        rx_str = '0'
        rx_message = 0
                #print("wait for server reply\n")
        while rx_str != '```\n':
                rx_message = cybot.readline()      # Wait for a message, readline expects message to end with "\n"
                rx_str = rx_message.decode()
                if rx_str != '```\n':
                        message.append(rx_str)
                         # Convert message from bytes to String (i.e., decode)
                else:
                        break
        label.config(text = message[1])

def to_object():
        send_message = 'h\n'
        cybot.write(send_message.encode())
        message = []
        rx_str = '0'
        rx_message = 0
                #print("wait for server reply\n")
        while rx_str != '```\n':
                rx_message = cybot.readline()      # Wait for a message, readline expects message to end with "\n"
                rx_str = rx_message.decode()
                if rx_str != '```\n':
                        message.append(rx_str)
                         # Convert message from bytes to String (i.e., decode)
                else:
                        break
        label.config(text = message[1])

canvas_widgets = []
window = tk.Tk()
window.title("GUI")
window.geometry("1600x960")

canvas = tk.Canvas(window)
scrollbar = tk.Scrollbar(window, orient ="vertical", command = canvas.yview)
scrollable_frame = tk.Frame(canvas)
scrollable_frame.bind(
        "<Configure>",
        lambda e: canvas.configure(scrollregion=canvas.bbox("all"))
)
canvas.create_window((0,0), window = scrollable_frame, anchor="nw")
canvas.configure(yscrollcommand=scrollbar.set)
canvas.pack(side = "left", fill = "both", expand = True)
scrollbar.pack(side="right", fill="y")
label = tk.Label(scrollable_frame, text = "Waiting for Scan data...")
label.pack(padx=20, pady=20)
# Define a common style for all buttons
button_style = {
    "font": ("Arial", 12, "bold"),  # Font style: Arial, size 12, bold
    "bg": "#4CAF50",                # Background color: green
    "fg": "white",                  # Foreground (text) color: white
    "activebackground": "#45a049",  # Button color when pressed/hovered
    "activeforeground": "white",    # Text color when pressed
    "padx": 20,                     # Horizontal padding
    "pady": 10,                     # Vertical padding
    "borderwidth": 3,               # Border thickness
    "relief": "raised"              # Button relief (3D effect)
}

# Apply the new style to all buttons
Exit_button = tk.Button(window, text="Exit", command=window.quit, **button_style)
Exit_button.pack(anchor="ne", padx=20, pady=10)

Scan_button = tk.Button(window, text="Scan", command=scan, **button_style)
Scan_button.pack(padx=10, pady=10)

Move_button = tk.Button(window, text="Move to Object", command=to_object, **button_style)
Move_button.pack(padx=10, pady=10)

Forward_button = tk.Button(window, text="Move Forward", command=forward, **button_style)
Forward_button.pack(padx=10, pady=10)

Left_button = tk.Button(window, text="Turn Left", command=left, **button_style)
Left_button.pack(padx=10, pady=10)

Right_button = tk.Button(window, text="Turn Right", command=right, **button_style)
Right_button.pack(padx=10, pady=10)

Backward_button = tk.Button(window, text="Move Backward", command=backward, **button_style)
Backward_button.pack(padx=10, pady=10)

Toggle_button = tk.Button(window, text="Toggle Mode", command=toggleMode, **button_style)
Toggle_button.pack(padx=10, pady=10)

window.mainloop()