Project: Implement a vending machine.
Author: Sapna Dasarath

Contents:
--------
	[1] Files
	[2] Usage 
	[3] Design
	[4] Testing

Files
------
[1] README.txt
	* This contains information about the project and directions to compile and run the program.
	
[2] build.xml
	* This is used to compile the java files.
	*  After the build the jar is generated in  build/jar folder. 
	*  Run java -jar VendingMachine.jar to run the program

[3] Java Files
	* VMProperties.java
	* VendingMachine.java
	* VMOperations.java
	* VMProductInfo.java
	* VMController.java
	* VMLauncher.java
	* VMFrame.java
	* VMHelp.java
	* VMInsertCoins.java
	* VMRefill.java
	* VMUser.java	  
	* VMEmployee.java
	* VMBuyer.java

Usage
-----
Compile and run using Ant

[1] Compile 
	* Unzip the orbitz folder.
		You can see src folder and build file along with this README file.
	* Use the build file attached to generate the required VendingMachine.jar
		run the following command on Linux or Windows.
		Ensure the ant home and Java home are updated in classpath.

	Command: ant clean-build

		sapna@ubuntu:~/orbitz$ ant clean-build
		Buildfile: build.xml

		clean:
   		[delete] Deleting directory /home/sapna/orbitz/build

		compile:
    		[mkdir] Created dir: /home/sapna/orbitz/build/classes
    		[javac] Compiling 13 source files to /home/sapna/orbitz/build/classes

		jar:
    		[mkdir] Created dir: /home/sapna/orbitz/build/jar
      		[jar] Building jar: /home/sapna/orbitz/build/jar/VendingMachine.jar

		clean-build:

		BUILD SUCCESSFUL
		Total time: 1 second


[2] Run
	* After compilation go to build/jar folder.
	* Click on the VendingMachine.jar to start the program if jar file association is done
		else run the following command

	Command: java -jar VendingMachine.jar 

		sapna@ubuntu:~/orbitz/build/jar$ java -jar VendingMachine.jar 
		Starting the vending machine program

Design
-------
[1] Hierarchy
--------------
	Main class
		* VMLauncher.java 		- Main class to launch the program.
	
	Utility
		* VMProperties.java 	- utility file.

	Controller
		* VMController.java		- Controller to interact between UI and Vending machine.
	
	UI
		* VMFrame.java			- Main UI Frame.
		* VMInsertCoins.java	- UI Frame to get user input to get coins.
		* VMRefill.java			- UI Frame to get the refill cans.
		* VMHelp.java			- UI Frame to show help on how to use the machine.
		
	Vending Machine
		* VMOperations.java 	- Interface.
		* VendingMachine.java	- Implements all the VMOperations.
		* VMProductInfo.java	- Used to maintain information about the products.
	
	Users
		* VMUser.java	  		- Interface.
		* VMEmployee.java		- Used to maintain the employee info.
		* VMBuyer.java			- Used to maintain the cash information about user.
		
[2] Description of files
------------------------
	VMProperties.java
	* This class is used to load the properties file and set the default values for the program.

	VendingMachine.java
	* This class implements the vending machine interface.It has the operations supported by user
	and employee.

	VMOperations.java
	* This class defines the interface for the operations valid on the Vending Machine.

	VMProductInfo.java
	* This class contains the product information.
 
	VMController.java
	* This class is the interface between the UI and the vending machine class.
 
	VMLauncher.java
	* This class is used to launch the application. It is the main class.
 
	VMFrame.java
	* This is the main class that renders the UI and shows user the current balance and soda
	counts
  
	VMHelp.java
	* This class shows the usage of the program
  
	VMInsertCoins.java
	* This class get the input from user to update cash
   
	VMRefill.java
	* This class gets input from user to refill soda cans
    
	VMUser.java
	* This class defines the user interface.


Testing
-------
[1] Story 1
	As an employee of a soda company I would like to be able to stock it with product. Done when I can stock
	the machine with product.

	Output: 
		Enter the values required as shown in user documentation.
		Enter invalid values like characters, error message is shown.
		Enter count greater than max value, error message is shown.

[2] Story 2
	As a user of the vending machine I need a way to insert money. Done when the machine will update balance
	correctly with value of money inserted.

	Output: 
		Enter the values required as shown in user documentation.
		Enter invalid values like characters, error message is shown.

[3] Story 3
	As a user of the vending machine I need to be able to purchase a product by pressing the appropriate 
	button for that product. 
	Done when pressing a product button dispenses the appropriate can and updates the user balance.

	Output: 
		Select the buttons as shown in user documentation.
		Enter invalid values and selecting multiple characters will show an error message.

[4] Story 4
	As a user of the vending machine I need to be able to collect my remaining balance at any point. 
	Done when pressing the "Coin Return" button returns the minimal number of coins to the user, equal
	to the user balance, from an infinite supply of quarters, dimes and nickels.

	Output: 
		Select the buttons as shown in user documentation.
		If there is any change it should be shown on the screen.

[5] Story 5 (Testing concurrency knowledge - bonus question)
	As a user of the vending machine I tend to press the product button more than once,really intending
	to purchase only one item (just being impatient). 
	Done when pressing the desired product button more than once only executes one product purchase and
	next purchase request is possible only after the purchased product from the previous request is dispensed

	Output: 
		The button and frame are implemented as part of the EDT thread. 
		Hence the button event will be received back to back.
		the first event clears the selection, hence the second event will not dispense any can.

