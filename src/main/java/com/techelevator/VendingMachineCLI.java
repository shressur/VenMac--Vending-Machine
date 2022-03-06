package com.techelevator;

import com.techelevator.view.Effects;
import com.techelevator.view.Machine;
import com.techelevator.view.Menu;

import java.util.List;
import java.util.Map;

public class VendingMachineCLI {

	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE, MAIN_MENU_OPTION_EXIT };

	private static final String PURCHASE_MENU_OPTION_FEED_MONEY = "Feed Money";
	private static final String PURCHASE_MENU_OPTION_SELECT_PRODUCT = "Select Product";
	private static final String PURCHASE_MENU_OPTION_FINISH_TRANSACTION = "Finish Transaction";
	//private static final String PURCHASE_MENU_OPTION_BACK_TO_MAIN_MENU = "Back to main menu";
	private static final String[] PURCHASE_MENU_OPTIONS = { PURCHASE_MENU_OPTION_FEED_MONEY, PURCHASE_MENU_OPTION_SELECT_PRODUCT, PURCHASE_MENU_OPTION_FINISH_TRANSACTION };


	private Menu menu;

	public VendingMachineCLI(Menu menu) {
		this.menu = menu;
	}

	public void run() {
		Machine vm = new Machine();
		String choice;
		String inventoryFilePath = "inventory.csv";
		Map<String, List<String>> myInventories;
		myInventories = vm.listInventories(inventoryFilePath);


		boolean isRunning = true;
		System.out.println("--------- MAIN MENU ---------");

		mainmenu: while (isRunning) {
			choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			System.out.println("Action: " + choice);

			switch (choice) {
				case MAIN_MENU_OPTION_DISPLAY_ITEMS:
					// display vending machine items
					vm.displayItems(myInventories);
					break;
				case MAIN_MENU_OPTION_PURCHASE:
					System.out.println("--------- PURCHASE MENU ---------");
					//stay in the loop
					while (true){
						choice = (String) menu.getChoiceFromOptions(PURCHASE_MENU_OPTIONS);
						System.out.println("Action: " + choice);
						switch (choice){
							case PURCHASE_MENU_OPTION_FEED_MONEY:
								//feed money
								vm.feedMoney();
								System.out.println("current balance: $" + vm.getBalance());
								//System.out.println();
								break;
							case PURCHASE_MENU_OPTION_SELECT_PRODUCT:
								vm.purchaseSelected(myInventories);
								break;
							case PURCHASE_MENU_OPTION_FINISH_TRANSACTION:
								vm.finishTransaction();
								//break;
								continue mainmenu;

							//case PURCHASE_MENU_OPTION_BACK_TO_MAIN_MENU:
							//	continue mainmenu;
						}
					} //purchase loop

				case MAIN_MENU_OPTION_EXIT:
					//exit
					System.out.println("--------- Thank You for using VenMac2.0 ---------");

					isRunning = false;
					break;
			}
		}	//main while loop
	}






//main
	public static void main(String[] args) {
		Menu menu = new Menu(System.in, System.out);
		VendingMachineCLI cli = new VendingMachineCLI(menu);

		//splash screen
		Machine.greetUser();
		try{
			String loadingMsg = "Loading... ";
			System.out.print(loadingMsg);
			Effects.spinnerEffect("/\\", 10, 200);
			System.out.print("\b".repeat(loadingMsg.length() + 1));
		} catch (InterruptedException ex){
			String errMsg = "Oops, something went wrong. Please, try again.";
			Effects.typeWriterEffect(errMsg, "_", 50);
		}

		//main screen
		String lorem = "WELCOME TO VenMac V2.0";
		Effects.typeWriterEffect(lorem, "_", 50);
		System.out.println();


		cli.run();
	}
}
