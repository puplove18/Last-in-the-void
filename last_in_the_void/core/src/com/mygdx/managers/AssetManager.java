package com.mygdx.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import java.util.HashMap;
import java.util.Map;

public class AssetManager implements Disposable {
    private final TextureAtlas resourcesAtlas;
    private final Map<String, TextureRegion> itemIcons = new HashMap<>();

  
    public AssetManager() {
        resourcesAtlas = new TextureAtlas(Gdx.files.internal("resources.atlas"));
        loadIcons();
    }
    public void loadIcons() {

        itemIcons.put("Common Biomass", resourcesAtlas.findRegion("common_biomass"));
        itemIcons.put("Uncommon Biomass", resourcesAtlas.findRegion("uncommon_biomass"));
        itemIcons.put("Rare Biomass", resourcesAtlas.findRegion("rare_biomass"));
        itemIcons.put("Legendary Biomass", resourcesAtlas.findRegion("epic_biomass"));
        
 
        itemIcons.put("Common Fuel", resourcesAtlas.findRegion("common_fuel"));
        itemIcons.put("Uncommon Fuel", resourcesAtlas.findRegion("uncommon_fuel"));
        itemIcons.put("Rare Fuel", resourcesAtlas.findRegion("rare_fuel"));
        itemIcons.put("Legendary Fuel", resourcesAtlas.findRegion("epic_fuel"));
    
        itemIcons.put("Common Building Materials", resourcesAtlas.findRegion("common_building_material"));
        itemIcons.put("Uncommon Building Materials", resourcesAtlas.findRegion("uncommon_building_material"));
        itemIcons.put("Rare Building Materials", resourcesAtlas.findRegion("rare_building_material")); 
        itemIcons.put("Legendary Building Materials", resourcesAtlas.findRegion("epic_building_material"));
    }

    public TextureRegionDrawable getItemIcon(String itemName) {
        TextureRegion region = itemIcons.get(itemName);
        

        if (region == null) {
            region = resourcesAtlas.findRegion("defaultIcon");
            if (region == null) {
                region = resourcesAtlas.getRegions().first();
            }
        }
        
        return new TextureRegionDrawable(region);
    }

    @Override
    public void dispose() {
        resourcesAtlas.dispose();
    }
}