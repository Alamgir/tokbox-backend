package com.cboxgames.idonia.backend.commons.accessorygenerator;

import java.util.*;

import com.cboxgames.idonia.backend.commons.Constants;
import com.cboxgames.utils.idonia.types.Accessory;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 12/5/11
 * Time: 2:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class AccessoryGenerator extends Constants implements IAccessoryGenerator {

    private static HashMap<String, Integer> set_warrior_attributes_for_rarity() {
        HashMap<String, Integer> weights = new HashMap<String, Integer>();
        weights.put(STR,50);
        weights.put(VIT,50);
        weights.put(AGI,35);
        weights.put(INTEL,0);
        weights.put(WIL,25);

        weights.put(ARMOR,15);
        weights.put(DODGE,15);
        weights.put(PCRIT,10);
        weights.put(SCRIT,0);
        return weights;
    }

    private static HashMap<String, Integer> set_healer_attributes_for_rarity() {
        HashMap<String, Integer> weights = new HashMap<String, Integer>();
        weights.put(STR,0);
        weights.put(VIT,40);
        weights.put(AGI,0);
        weights.put(INTEL,55);
        weights.put(WIL,55);

        weights.put(ARMOR,10);
        weights.put(DODGE,10);
        weights.put(PCRIT,0);
        weights.put(SCRIT,30);        
        return weights;
    }

    private static HashMap<String, Integer> set_rogue_attributes_for_rarity() {
        HashMap<String, Integer> weights = new HashMap<String, Integer>();
        weights.put(STR,50);
        weights.put(VIT,40);
        weights.put(AGI,50);
        weights.put(INTEL,0);
        weights.put(WIL,20);

        weights.put(ARMOR,11);
        weights.put(DODGE,11);
        weights.put(PCRIT,18);
        weights.put(SCRIT,0);
        return weights;
    }

    private static HashMap<String, Integer> set_archer_attributes_for_rarity() {
        HashMap<String, Integer> weights = new HashMap<String, Integer>();
        weights.put(STR,0);
        weights.put(VIT,40);
        weights.put(AGI,60);
        weights.put(INTEL,40);
        weights.put(WIL,20);

        weights.put(ARMOR,10);
        weights.put(DODGE,10);
        weights.put(PCRIT,20);
        weights.put(SCRIT,0);
        return weights;
    }

    private static HashMap<String, Integer> set_mage_attributes_for_rarity() {
        HashMap<String, Integer> weights = new HashMap<String, Integer>();
        weights.put(STR,0);
        weights.put(VIT,40);
        weights.put(AGI,0);
        weights.put(INTEL,60);
        weights.put(WIL,40);

        weights.put(ARMOR,15);
        weights.put(DODGE,15);
        weights.put(PCRIT,0);
        weights.put(SCRIT,30);
        return weights;
    }

    public static Accessory generateAccessoryForCharacter(String character_name, int level, String rare) {
        String rarity;
        if (!rare.equals(RANDOM)) {
            rarity = rare;
        } else {
            rarity = returnRarity();
        }

        Accessory accessory_stats = new Accessory();
        accessory_stats.level_requirement = level;
        accessory_stats.rarity = rarity;

        //Set the Display order
        if (rarity.equals(COMMON)) {
            accessory_stats.display_order = 1;
        }
        else if (rarity.equals(UNIQUE)) {
            accessory_stats.display_order = 2;
        }
        else if (rarity.equals(EPIC)) {
            accessory_stats.display_order = 3;
        }
        else {
            throw new IllegalArgumentException("Didn't find accessory with rarity of" + rare);
        }
        HashMap<String, Integer> weights;

        if (character_name.equals(WARRIOR)) {
            accessory_stats.name = accessory_stats.description = RING;
            weights = set_warrior_attributes_for_rarity();
        }
        else if (character_name.equals(HEALER)) {
            accessory_stats.name = accessory_stats.description = NECKLACE;
            weights = set_healer_attributes_for_rarity();
        }
        else if (character_name.equals(ROGUE)) {
            accessory_stats.name = accessory_stats.description = EARRINGS;
            weights = set_rogue_attributes_for_rarity();
        }
        else if (character_name.equals(ARCHER)) {
            accessory_stats.name = accessory_stats.description = TALISMAN;
            weights = set_archer_attributes_for_rarity();
        }
        else if (character_name.equals(MAGE)) {
            accessory_stats.name = accessory_stats.description = ORB;
            weights = set_mage_attributes_for_rarity();
        }
        else {
            throw new IllegalArgumentException("Didn't find character with name of " + character_name);
        }

        int total = TOTAL_POINTS;
        double total_stat_points = level*POINTS_PER_LEVEL;

        Random rand = new Random();
        for (int x=0; x<(int)total_stat_points; x++) {
            // int value = (int)((Math.random()*total)+1);
        	int value = rand.nextInt(total) + 1;
            String key = return_stat_value_for_number(weights, value);
            accessory_stats.cost += weights.get(key);

            incrementStatPointForAccessory(accessory_stats, key, character_name, rarity);
        }

        if (rarity.equals(COMMON)) {
            accessory_stats.cost *= 1.5;
        }
        else if (rarity.equals(UNIQUE)) {
            accessory_stats.cost *= 4.0;
        }
        else if (rarity.equals(EPIC)) {
            accessory_stats.cost *= 6.5;
        }
        accessory_stats.accessory_type = ACCESSORY;

        return accessory_stats;

    }

    public static String returnRarity() {
        int common = 800;
        int unique = 150;
        Random rand = new Random();
        // double rarity = ((Math.random()*1000)+1);
        int rarity = rand.nextInt(1000) + 1;
        int total = common;
        if (rarity <= total) { return COMMON; }
        total += unique;
        if (rarity <= total) { return UNIQUE; }
        return EPIC;
    }

    private static String return_stat_value_for_number(HashMap<String, Integer> weights, Integer num) {
        int total = 0;
        for (Map.Entry<String, Integer> weight : weights.entrySet()) {
            total += weight.getValue();
            if (total >= num) { return weight.getKey(); }
        }
        return null;
    }

    private static void incrementStatPointForAccessory(Accessory accessory_stats, String key, String character_name, String rarity) {
        double points_per_str = 0;
        double points_per_vit = 0;
        double points_per_agi = 0;
        double points_per_intel = 0;
        double points_per_wil = 0;
        double points_per_armor = 0;
        double points_per_dodge = 0;
        double points_per_pcrit = 0;
        double points_per_scrit = 0;
        
        if (character_name.equals(WARRIOR)) {
            if (rarity.equals(COMMON)) {
                 points_per_str = 1.0;
                 points_per_vit = 1.0;
                 points_per_agi = 1.0;
                 points_per_intel = 0.0;
                 points_per_wil = 1.0;
    
                 points_per_armor = 1.0;
                 points_per_dodge = 0.4;
                 points_per_pcrit = 0.5;
                 points_per_scrit = 0.0;
            }
            else if (rarity.equals(UNIQUE)) {
                 points_per_str = 1.3;
                 points_per_vit = 1.4;
                 points_per_agi = 1.1;
                 points_per_intel = 0.0;
                 points_per_wil = 1.1;
    
                 points_per_armor = 1.3;
                 points_per_dodge = 0.6;
                 points_per_pcrit = 0.5;
                 points_per_scrit = 0.0;
            }
            else if (rarity.equals(EPIC)) {
                 points_per_str = 1.8;
                 points_per_vit = 2.0;
                 points_per_agi = 1.3;
                 points_per_intel = 0.0;
                 points_per_wil = 1.3;
    
                 points_per_armor = 1.8;
                 points_per_dodge = 0.8;
                 points_per_pcrit = 0.8;
                 points_per_scrit = 0.0;
            }
        }
        else if (character_name.equals(HEALER)) {
            if (rarity.equals(COMMON)) {
                 points_per_str = 0.0;
                 points_per_vit = 1.0;
                 points_per_agi = 0.0;
                 points_per_intel = 1.0;
                 points_per_wil = 1.0;
    
                 points_per_armor = 0.5;
                 points_per_dodge = 0.4;
                 points_per_pcrit = 0.0;
                 points_per_scrit = 1.0;
            }
            else if (rarity.equals(UNIQUE)) {
                 points_per_str = 0.0;
                 points_per_vit = 1.3;
                 points_per_agi = 0.0;
                 points_per_intel = 1.4;
                 points_per_wil = 1.4;
    
                 points_per_armor = 0.6;
                 points_per_dodge = 0.5;
                 points_per_pcrit = 0.0;
                 points_per_scrit = 1.3;
            }
            else if (rarity.equals(EPIC)) {
                 points_per_str = 0.0;
                 points_per_vit = 1.6;
                 points_per_agi = 0.0;
                 points_per_intel = 2.0;
                 points_per_wil = 1.8;
    
                 points_per_armor = 0.8;
                 points_per_dodge = 0.7;
                 points_per_pcrit = 0.0;
                 points_per_scrit = 1.8;
            }
            else {
                throw new IllegalArgumentException("Could not find accessory with rarity of " + rarity);
            }
        }
        else if (character_name.equals(ROGUE)) {
            if (rarity.equals(COMMON)) {
                 points_per_str = 1.0;
                 points_per_vit = 1.0;
                 points_per_agi = 1.0;
                 points_per_intel = 0.0;
                 points_per_wil = 1.0;
    
                 points_per_armor = 0.5;
                 points_per_dodge = 0.4;
                 points_per_pcrit = 1.0;
                 points_per_scrit = 0.0;
            }
            else if (rarity.equals(UNIQUE)) {
                 points_per_str = 1.3;
                 points_per_vit = 1.2;
                 points_per_agi = 1.4;
                 points_per_intel = 0.0;
                 points_per_wil = 1.1;
    
                 points_per_armor = 0.6;
                 points_per_dodge = 0.7;
                 points_per_pcrit = 1.3;
                 points_per_scrit = 0.0;
            }
            else if (rarity.equals(EPIC)) {
                 points_per_str = 1.8;
                 points_per_vit = 1.7;
                 points_per_agi = 2.0;
                 points_per_intel = 0.0;
                 points_per_wil = 1.5;
    
                 points_per_armor = 0.8;
                 points_per_dodge = 1.0;
                 points_per_pcrit = 1.8;
                 points_per_scrit = 0.0;
            }
            else {
                throw new IllegalArgumentException("Could not find accessory with rarity of " + rarity);
            }
        }
        else if (character_name.equals(ARCHER)) {
            if (rarity.equals(COMMON)) {
                 points_per_str = 0.0;
                 points_per_vit = 1.0;
                 points_per_agi = 1.0;
                 points_per_intel = 1.0;
                 points_per_wil = 1.0;
    
                 points_per_armor = 0.5;
                 points_per_dodge = 0.7;
                 points_per_pcrit = 0.5;
                 points_per_scrit = 0.5;
            }
            else if (rarity.equals(UNIQUE)) {
                 points_per_str = 0.0;
                 points_per_vit = 1.2;
                 points_per_agi = 1.6;
                 points_per_intel = 1.2;
                 points_per_wil = 1.2;
    
                 points_per_armor = 0.6;
                 points_per_dodge = 0.9;
                 points_per_pcrit = 0.7;
                 points_per_scrit = 0.7;
            }
            else if (rarity.equals(EPIC)) {
                 points_per_str = 0.0;
                 points_per_vit = 1.6;
                 points_per_agi = 2.0;
                 points_per_intel = 1.6;
                 points_per_wil = 1.6;
    
                 points_per_armor = 0.8;
                 points_per_dodge = 1.1;
                 points_per_pcrit = 1.0;
                 points_per_scrit = 1.0;
            }
            else {
                throw new IllegalArgumentException("Could not find accessory with rarity of " + rarity);
            }
        }
        else if (character_name.equals(MAGE)) {
            if (rarity.equals(COMMON)) {
                 points_per_str = 0.0;
                 points_per_vit = 1.0;
                 points_per_agi = 0.0;
                 points_per_intel = 1.0;
                 points_per_wil = 1.0;
    
                 points_per_armor = 0.5;
                 points_per_dodge = 0.4;
                 points_per_pcrit = 0.0;
                 points_per_scrit = 0.8;
            }
            else if (rarity.equals(UNIQUE)) {
                 points_per_str = 0.0;
                 points_per_vit = 1.2;
                 points_per_agi = 0.0;
                 points_per_intel = 1.6;
                 points_per_wil = 1.2;
    
                 points_per_armor = 0.6;
                 points_per_dodge = 0.5;
                 points_per_pcrit = 0.0;
                 points_per_scrit = 0.9;
            }
            else if (rarity.equals(EPIC)) {
                 points_per_str = 0.0;
                 points_per_vit = 1.5;
                 points_per_agi = 0.0;
                 points_per_intel = 2.0;
                 points_per_wil = 1.5;
    
                 points_per_armor = 0.8;
                 points_per_dodge = 0.7;
                 points_per_pcrit = 0.0;
                 points_per_scrit = 1.1;
            }
            else {
                throw new IllegalArgumentException("Could not find accessory with rarity of " + rarity);
            }
        }
        else {
            throw new IllegalArgumentException("Could not find character name of " + character_name);
        }

        if (key.equals(STR)) {
            accessory_stats.strength += points_per_str;
        }
        else if (key.equals(VIT)) {
            accessory_stats.vitality += points_per_vit;
        }
        else if (key.equals(AGI)) {
            accessory_stats.agility += points_per_agi;
        }
        else if (key.equals(INTEL)) {
            accessory_stats.intelligence += points_per_intel;
        }
        else if (key.equals(WIL)) {
            accessory_stats.will += points_per_wil;
        }
        else if (key.equals(ARMOR)) {
            accessory_stats.armor += points_per_armor;
        }
        else if (key.equals(DODGE)) {
            accessory_stats.dodge += points_per_dodge;
        }
        else if (key.equals(PCRIT)) {
            accessory_stats.physical_crit += points_per_pcrit;
        }
        else if (key.equals(SCRIT)) {
            accessory_stats.spell_crit += points_per_scrit;
        }
        else {
            throw new IllegalArgumentException("Could not find key with name: " + key);
        }
    }


}
