package com.cboxgames.idonia.backend.commons.db.mob;

import java.util.List;
import java.util.Map;

import com.cboxgames.utils.idonia.types.Mob;
import com.cboxgames.utils.idonia.types.MobSkill;
import com.cboxgames.utils.idonia.types.Mob.MobWrapper;
import com.cboxgames.utils.idonia.types.MobSkill.MobSkillWrapper;
import com.cboxgames.utils.idonia.types.MobSkillEffect.MobSkillEffectWrapper;

public interface IMobDB {

	/**
	 * Save a mob to the database.
	 * 
	 * @param mob
	 * @return
	 */
	public boolean createMob(Mob mob);
	
	/**
	 * Get a list of all mobs.
	 * 
	 * Does not include skills.
	 * 
	 * @return
	 */
	public List<Mob> getMobs();
	
	/**
	 * Get a list of mobs, mapped to their respective IDs.
	 * 
	 * Does not include skills.
	 * 
	 * @param mob_ids
	 * @return
	 */
	public Map<Integer, Mob> getMobs(List<Integer> mob_ids);
	
	/**
	 * Get a wrapper array of all mobs.
	 * 
	 * Does not include skills.
	 * 
	 * @return
	 */
	public MobWrapper[] getMobDetails();
	
	/**
	 * Get a list of mobs with given difficulty.
	 * 
	 * Include skills and skill_effects.
	 * 
	 * @param mob_ids
	 * @return
	 */
	
	public List<Mob> getMobsByDifficulty(String difficulty);
	
	/**
	 * Get a wrapper array of all mob_skills.
	 * 
	 * Does not include skills.
	 * 
	 * @return
	 */
	public MobSkillWrapper[] getMobSkillDetails();
	
	/**
	 * Get a wrapper array of all mob_skill_effects.
	 * 
	 * @return
	 */
	public MobSkillEffectWrapper[] getMobSkillEffectDetails();
	
	/**
	 * Get a list of all mob_skills.
	 * 
	 * Does not include skill_effects.
	 * 
	 * @return
	 */
	public List<MobSkill> getMobSkills();
}
