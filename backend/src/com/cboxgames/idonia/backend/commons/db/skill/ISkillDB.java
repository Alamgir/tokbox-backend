package com.cboxgames.idonia.backend.commons.db.skill;

import java.util.List;
import java.util.Map;

import com.cboxgames.utils.idonia.types.Skill;
import com.cboxgames.utils.idonia.types.Skill.SkillWrapper;
import com.cboxgames.utils.idonia.types.SkillEffect.SkillEffectWrapper;

public interface ISkillDB {

    public static int SKILL_TYPE_SPELL_ID = 0;
    public static int SKILL_TYPE_POTION_ID = 1;
	/**
	 * Retrieve all skills in the database.  This will also include skill effects.
	 * 
	 * @return
	 */
	public List<Skill> getSkills();
	
	/**
	 * Retrieve a skill from the database.  This will also include the skill effect.
	 * 
	 * @param id
	 * @return
	 */
	public Skill getSkill(int id);
	
	/**
	 * Get a list of skills for each character type, mapped by the character ID.
	 * This will also include the skill effects.
	 * 
	 * @param ids the list of character IDs.
	 * 
	 * @return a map of character ID and skills.
	 */
	public Map<Integer, List<Skill>> getSkillsByCharacter(List<Integer> ids);
	
	/**
	 * Get all skills.
	 *
	 * @return
	 */
	public SkillWrapper[] getSkillDetails();
	
	/**
	 * Get all skill_effects.
	 *
	 * @return
	 */
	public SkillEffectWrapper[] getSkillEffectDetails();
    
    public enum SkillType {
        Spell(SKILL_TYPE_SPELL_ID),
        Potion(SKILL_TYPE_POTION_ID);
        
        private int id;
        
        private SkillType(int id) {
            this.id = id;
        }
        
        public int getId() { return id; }
        
        public static String idToName(int id) {
            switch (id) {
                case SKILL_TYPE_SPELL_ID:
                    return Spell.name();
                case SKILL_TYPE_POTION_ID:
                    return Potion.name();
                default:
                    return "Spell";
            }
        }
    }
}
