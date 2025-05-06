package com.mygdx.helpers;

public enum ResourceType {
    COMMON_BIOMASS("Common Biomass"),
    UNCOMMON_BIOMASS("Uncommon Biomass"),
    RARE_BIOMASS("Rare Biomass"),
    LEGENDARY_BIOMASS("Legendary Biomass"),
    
    COMMON_FUEL("Common Fuel"),
    UNCOMMON_FUEL("Uncommon Fuel"),
    RARE_FUEL("Rare Fuel"),
    LEGENDARY_FUEL("Legendary Fuel"),
    
    COMMON_BUILDING_MATERIALS("Common Building Materials"),
    UNCOMMON_BUILDING_MATERIALS("Uncommon Building Materials"),
    RARE_BUILDING_MATERIALS("Rare Building Materials"),
    LEGENDARY_BUILDING_MATERIALS("Legendary Building Materials");
    
    private final String displayName;
    
    ResourceType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public static ResourceType fromDisplayName(String displayName) {
        for (ResourceType type : ResourceType.values()) {
            if (type.displayName.equals(displayName)) {
                return type;
            }
        }
        System.out.println("Invalid resource type: " + displayName);
        return null;
    }

    public static boolean isValidResource(String displayName) {
        boolean isValid = fromDisplayName(displayName) != null;
        System.out.println("Resource Validation: " + displayName + " - " + (isValid ? "Valid" : "Invalid"));
        return isValid;
    }
}
