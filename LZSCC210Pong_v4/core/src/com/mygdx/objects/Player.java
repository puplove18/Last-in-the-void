package com.mygdx.objects;


public class Player{
    private String name;
    private double health = 100;
    private int healthUpgradeLevel = 0; 
	private double fuel = 100;
    private int fuelUpgradeLevel = 0; 
    private double oxygen = 100;
    private int oxygenUpgradeLevel = 0; 
    private Inventory inventory;

    public double getFuelCapacity() {
        fuel *= 2;
        return fuel;
    }
    public double getHealthCapacity() {
        health *= 2;
        return health;
    }
    public double getOxygenCapacity() {
        oxygen *= 2;
        return oxygen;
    }
    public void upgradeFuel() {
        if (fuelUpgradeLevel < 4) { // Max 4 levels
            fuelUpgradeLevel++;
            System.out.println("Fuel upgraded! New capacity: " + getFuelCapacity());
        } else {
            System.out.println("Fuel fully upgraded!");
        }
    }
    public void upgradeOxygen() {
        if (healthUpgradeLevel < 4) { // Max 4 levels
            healthUpgradeLevel++;
            System.out.println("Oxygen upgraded! New health: " + getOxygenCapacity());
        } else {
            System.out.println("Oxygen fully upgraded!");   
        }
    }
    public void upgradeHealth() {
        if (oxygenUpgradeLevel < 4) { // Max 4 levels
            oxygenUpgradeLevel++;
            System.out.println("Health upgraded! New capacity: " + getHealthCapacity());
        } else {
            System.out.println("Health fully upgraded!");
        }
    }

    public enum Stats {
        HEALTH, FUEL, OXYGEN;
    }

    public Player() {
        this.inventory = new Inventory(24);

        // test items in the beginning
        this.inventory.addItem("Common Metal", 5);
        this.inventory.addItem("Oxygen", 10);
        this.inventory.addItem("Fuel", 8);
    }

    public void setHealth(double val) {
    if (val < 0) {
        this.health = 0;
    } else if (val > 100) {
        this.health = 100;
    } else {
        this.health = val;
    }
}

    public double getHealth() {
        return (double) this.health;
    }

	public void setFuel(double val) {
    if (val < 0) {
        this.fuel = 0;
    } else if (val > 100) {
        this.fuel = 100;
    } else {
        this.fuel = val;
    }
}

    public double getFuel() {
        return (double) this.fuel;
    }

    public void setOxygen(double val) {
    if (val < 0) {
        this.oxygen = 0;
    } else if (val > 100) {
        this.oxygen = 100;
    } else {
        this.oxygen = val;
    }
}

    public double getOxygen() {
        return (double) this.oxygen;
    }    

    public void setName(String playerName) {
    if (playerName != null && !playerName.isEmpty()) {
        this.name = playerName;
    } else {
        System.out.println("Error");
    }
}

	public void updateStat(Stats stat, double val) {
		double old_val;
		double new_val;
		if (stat == Stats.HEALTH) {
			old_val = this.health;
			new_val = old_val + val;
			this.setHealth(new_val);
		}
		else if (stat == Stats.FUEL) {
			old_val = this.fuel;
			new_val = old_val + val;
			this.setFuel(new_val);
		}
		else if (stat == Stats.OXYGEN) {
			old_val = this.oxygen;
			new_val = old_val + val;
			this.setOxygen(new_val);
		}	
	}


    // Interaction with Inventory
    public void addItemToInventory(String item) {
        inventory.addItem(item);
    }

    // Additional method in cases where we need to add multiple items, you can delete the old method if you want
    public void addItemToInventory(String item, int quantity) { // Added quantity parameter
        if (inventory != null) { // Good practice to check if inventory exists
           inventory.addItem(item, quantity); // Call inventory's method with quantity
        } else {
            System.err.println("Player inventory not initialized, cannot add item: " + item);
        }
    }

    public void removeItemFromInventory(String item, int quantity) {
        inventory.removeItem(item, quantity);
    }

    public void showInventory() {
		inventory.showInventory();
	}

    public Inventory getInventory() {
        return inventory;
    }

    
    public static void main(String[] args){
        Player tester = new Player();

        Stats health = Stats.HEALTH;
        Stats fuel = Stats.FUEL;
        Stats oxygen = Stats.OXYGEN;

        tester.updateStat(health, -40);
        tester.updateStat(fuel, -37.5);
        tester.updateStat(oxygen, 30);

        System.out.println(tester.getHealth());
        System.out.println(tester.getFuel());
        System.out.println(tester.getOxygen());

        

    }
}
