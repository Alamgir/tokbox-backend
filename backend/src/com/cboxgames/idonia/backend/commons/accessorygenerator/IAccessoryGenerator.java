package com.cboxgames.idonia.backend.commons.accessorygenerator;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 12/5/11
 * Time: 2:08 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IAccessoryGenerator {

    public class AccessoryStats {
        public int strength = 0;
        public int vitality = 0;
        public int agility = 0;
        public int intelligence = 0;
        public int will = 0;
        public int armor = 0;
        public int dodge = 0;
        public int physical_crit = 0;
        public int spell_crit = 0;
        public String name;
        public String description;
        public int cost = 0;
        public int level_requirement;
        public String rarity;
        public String accessory_type;
        public int display_order;

        public void init() {}
    }



}
