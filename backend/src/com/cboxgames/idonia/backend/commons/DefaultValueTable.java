package com.cboxgames.idonia.backend.commons;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.servlet.ServletContext;

import com.cboxgames.utils.idonia.types.Accessory;
import com.cboxgames.utils.idonia.types.Mob;
import com.cboxgames.utils.idonia.types.Accessory.AccessoryWrapper;
import com.cboxgames.utils.idonia.types.AccessorySkill.AccessorySkillWrapper;
import com.cboxgames.utils.idonia.types.Achievement.AchievementWrapper;
import com.cboxgames.utils.idonia.types.Analytic.AnalyticWrapper;

import com.cboxgames.utils.idonia.types.AccessorySkillEffect.AccessorySkillEffectWrapper;
import com.cboxgames.utils.idonia.types.Banner.BannerWrapper;
import com.cboxgames.utils.idonia.types.Character;
import com.cboxgames.utils.idonia.types.Character.CharacterWrapper;
import com.cboxgames.utils.idonia.types.Mob.MobWrapper;
import com.cboxgames.utils.idonia.types.MobSkill.MobSkillWrapper;
import com.cboxgames.utils.idonia.types.MobSkillEffect.MobSkillEffectWrapper;
import com.cboxgames.utils.idonia.types.Playlist.PlaylistWrapper;
import com.cboxgames.utils.idonia.types.Purchase.PurchaseWrapper;
import com.cboxgames.utils.idonia.types.Skill;
import com.cboxgames.utils.idonia.types.Skill.SkillWrapper;
import com.cboxgames.utils.idonia.types.SkillEffect;
import com.cboxgames.utils.idonia.types.SkillEffect.SkillEffectWrapper;

import com.cboxgames.utils.idonia.types.Node.NodeWrapper;
import com.cboxgames.utils.idonia.types.Tutorial.TutorialWrapper;
import com.cboxgames.utils.json.JsonConverter;

import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyException;

import com.cboxgames.idonia.backend.listener.DefaultValueTableWatchListener;

/**
 * Loading and reloading of static tables.
 * Author: Michael Chang
 */

public class DefaultValueTable {
	
	private static final String _sub_path = "/WEB-INF/lookup/";
	private static final String _accessories_json = "Accessories.json";
	private static final String _accessory_skills_json = "AccessorySkills.json";
	private static final String _accessory_skill_effects_json = "AccessorySkillEffects.json";
	private static final String _achievements_json = "Achievements.json";
    private static final String _analytics_json = "Analytics.json";
    private static final String _banners_json = "Banners.json";
    private static final String _characters_json = "Characters.json";
    private static final String _mobs_json = "Mobs.json";
    private static final String _nodes_json = "Nodes.json";
    private static final String _playlists_json = "Playlists.json";
    private static final String _purchases_json = "Purchases.json";
    private static final String _skills_json = "Skills.json"; 
    private static final String _skill_effects_json = "SkillEffects.json"; 
    private static final String _tutorials_json = "Tutorials.json";
    private static final String _mob_skills = "MobSkills.json";
    private static final String _mob_skill_effects = "MobSkillEffects.json";

    private static AccessoryWrapper[] _accessory_wrapper_array;
    private static AccessorySkillWrapper[] _accessory_skill_wrapper_array;
    private static AccessorySkillEffectWrapper[] _accessory_skill_effect_wrapper_array;
    private static AchievementWrapper[] _achievement_wrapper_array;
    private static AnalyticWrapper[] _analytic_wrapper_array;
    private static BannerWrapper[] _banner_wrapper_array;
    private static CharacterWrapper[] _character_wrapper_array;
    private static MobWrapper[] _mob_wrapper_array;
    private static NodeWrapper[] _node_wrapper_array;
    private static PlaylistWrapper[] _playlist_wrapper_array;
    private static PurchaseWrapper[] _purchase_wrapper_array;
    static private SkillWrapper[] _skill_wrapper_array;
    static private SkillEffectWrapper[] _skill_effect_wrapper_array;
    private static TutorialWrapper[] _tutorial_wrapper_array;
    private static MobSkillWrapper[] _mob_skills_wrapper_array;
    private static MobSkillEffectWrapper[] _mob_skills_effect_wrapper_array;
    
 	private static JsonConverter _json_converter;
 	private static ServletContext _servlet_context;

    public static AccessoryWrapper[] getAccessoryWrapperArray() { return _accessory_wrapper_array; }
    public static AccessorySkillWrapper[] getAccessorySkillWrapperArray() { return _accessory_skill_wrapper_array; }
    public static AccessorySkillEffectWrapper[] getAccessorySkillEffectWrapperArray() { return _accessory_skill_effect_wrapper_array; }
    public static AchievementWrapper[] getAchievementWrapperArray() { return _achievement_wrapper_array; }
    public static AnalyticWrapper[] getAnalyticWrapperArray() { return _analytic_wrapper_array; }
    public static BannerWrapper[] getBannerWrapperArray() { return _banner_wrapper_array; }
    public static CharacterWrapper[] getCharacterWrapperArray() { return _character_wrapper_array; }
    public static MobWrapper[] getMobWrapperArray() { return _mob_wrapper_array; }
    public static NodeWrapper[] getNodeWrapperArray() { return _node_wrapper_array; }
    public static PlaylistWrapper[] getPlaylistWrapperArray() { return _playlist_wrapper_array; }
    public static PurchaseWrapper[] getPurchaseWrapperArray() { return _purchase_wrapper_array; }
    public static SkillWrapper[] getSkillWrapperArray() { return _skill_wrapper_array; }
    public static SkillEffectWrapper[] getSkillEffectWrapperArray() { return _skill_effect_wrapper_array; }
    public static TutorialWrapper[] getTutorialWrapperArray() {  return _tutorial_wrapper_array; }
    public static MobSkillWrapper[] getMobSkillWrapper() { return _mob_skills_wrapper_array; }
    public static MobSkillEffectWrapper[] getMobSkillEffectWrapper() { return _mob_skills_effect_wrapper_array; }
    
 	private int _watch_id;
 	
    public static JsonConverter getJsonConverter() { return _json_converter; }
    public static ServletContext getServletContext() { return _servlet_context; }
    public int getWatchId() { return _watch_id; }
    
	public DefaultValueTable(JsonConverter json_converter, ServletContext servlet_context) {
		_json_converter = json_converter;
		_servlet_context = servlet_context;
	}
	
	public void createAllDefaultValueTable() {
		
		_accessory_wrapper_array = createDefaultValueTable(AccessoryWrapper[].class, _sub_path + _accessories_json);
		_accessory_skill_wrapper_array = createDefaultValueTable(AccessorySkillWrapper[].class, _sub_path + _accessory_skills_json);
		_accessory_skill_effect_wrapper_array = createDefaultValueTable(AccessorySkillEffectWrapper[].class, _sub_path + _accessory_skill_effects_json);
		_analytic_wrapper_array = createDefaultValueTable(AnalyticWrapper[].class, _sub_path + _analytics_json);
		_achievement_wrapper_array = createDefaultValueTable(AchievementWrapper[].class, _sub_path + _achievements_json);
		_banner_wrapper_array = createDefaultValueTable(BannerWrapper[].class, _sub_path + _banners_json);
		_character_wrapper_array = createDefaultValueTable(CharacterWrapper[].class, _sub_path + _characters_json);
		_mob_wrapper_array = createDefaultValueTable(MobWrapper[].class, _sub_path + _mobs_json);
		_node_wrapper_array = createDefaultValueTable(NodeWrapper[].class, _sub_path + _nodes_json);
		_playlist_wrapper_array = createDefaultValueTable(PlaylistWrapper[].class, _sub_path + _playlists_json);
		_purchase_wrapper_array = createDefaultValueTable(PurchaseWrapper[].class, _sub_path + _purchases_json);
		_skill_wrapper_array = createDefaultValueTable(SkillWrapper[].class, _sub_path + _skills_json);
		_skill_effect_wrapper_array = createDefaultValueTable(SkillEffectWrapper[].class, _sub_path + _skill_effects_json);
		_tutorial_wrapper_array = createDefaultValueTable(TutorialWrapper[].class, _sub_path + _tutorials_json);
		_mob_skills_wrapper_array = createDefaultValueTable(MobSkillWrapper[].class, _sub_path + _mob_skills);
		_mob_skills_effect_wrapper_array = createDefaultValueTable(MobSkillEffectWrapper[].class, _sub_path + _mob_skill_effects);
		
		addSkillToCharacter();
		addAccessoryToCharacter();
		
		try {
			setupWatchListener();
		} catch (JNotifyException e) {
			e.printStackTrace();
		}
	}
	
	private static String readJsonFile(String file_path) {

    	String rets = null;
    	
        try {
        	StringBuilder text = new StringBuilder();
            InputStream stream = _servlet_context.getResourceAsStream(file_path);
            if (stream != null) {
                Scanner scanner = new Scanner(stream , "UTF-8");
                try {
                    while (scanner.hasNextLine()) {
                        text.append(scanner.nextLine());
                    }
                    
                    rets = text.toString();
                }
                finally {
                    scanner.close();
                }
            }
            else {
                throw new FileNotFoundException("Could not find " + file_path);
            }

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return rets;
    }
	
	private static <T> T[] createDefaultValueTable(Class<T[]> class_type, String file_path) {

		T[] wrapper_array = null;
		String json_data = readJsonFile(file_path);
	
		try {
			wrapper_array = getJsonConverter().getObject(json_data, class_type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return wrapper_array;
	}
	
	private static void addSkillToCharacter() {
		
		for (int iy = 0; iy < _character_wrapper_array.length; iy++) {
			Character ch = _character_wrapper_array[iy].character;
			List<Skill> skill_list = new ArrayList<Skill>();
			for (int ix = 0; ix < _skill_wrapper_array.length; ix++) {
				Skill sk = _skill_wrapper_array[ix].skill;
				if (sk.character_id != ch.id) continue;	
				skill_list.add(sk);
			}
			
			if (skill_list.isEmpty() == true) continue;
			
			ch.user_character_skills = (Skill[]) skill_list.toArray(new Skill[0]);
		}
	}
	
	private static void addAccessoryToCharacter() {
		
		for (int iy = 0; iy < _character_wrapper_array.length; iy++) {
			Character ch = _character_wrapper_array[iy].character;
			List<Accessory> accessory_list = new ArrayList<Accessory>();
			for (int ix = 0; ix < _accessory_wrapper_array.length; ix++) {
				Accessory ac = _accessory_wrapper_array[ix].accessory;
				if (ac.character_id != ch.id) continue;	
				accessory_list.add(ac);
			}
			
			if (accessory_list.isEmpty() == true) continue;
			
			ch.user_character_accessories = (Accessory[]) accessory_list.toArray(new Accessory[0]);
		}
	}

	private void setupWatchListener() throws JNotifyException {
		
		String path = _servlet_context.getRealPath(_sub_path);
		int mask = JNotify.FILE_MODIFIED
//				   | JNotify.FILE_CREATED
//			       | JNotify.FILE_DELETED
//			       | JNotify.FILE_RENAMED
	               ;

	    boolean watchSubtree = false;
	    _watch_id = JNotify.addWatch(path, mask, watchSubtree, new DefaultValueTableWatchListener());
	}
	
	public static boolean reloadDefaultValueTable(String filename) {
		
		boolean reload = true;
		if (filename.equals(_accessories_json)) {
			_accessory_wrapper_array = createDefaultValueTable(AccessoryWrapper[].class, _sub_path + _accessories_json);
			addAccessoryToCharacter();
		}
		else if (filename.equals(_accessory_skills_json)) {
			_accessory_skill_wrapper_array = createDefaultValueTable(AccessorySkillWrapper[].class, _sub_path + _accessory_skills_json);
		}
		else if (filename.equals(_achievements_json)) {
			_achievement_wrapper_array = createDefaultValueTable(AchievementWrapper[].class, _sub_path + _achievements_json);
		}
	    else if (filename.equals(_analytics_json)) {
			_analytic_wrapper_array = createDefaultValueTable(AnalyticWrapper[].class, _sub_path + _analytics_json);
	    }
	    else if (filename.equals(_banners_json)) {
			_banner_wrapper_array = createDefaultValueTable(BannerWrapper[].class, _sub_path + _banners_json);
	    }
	    else if (filename.equals(_characters_json)) {
			_character_wrapper_array = createDefaultValueTable(CharacterWrapper[].class, _sub_path + _characters_json);
			addSkillToCharacter();
			addAccessoryToCharacter();
	    }
	    else if (filename.equals(_mobs_json)) {
			_mob_wrapper_array = createDefaultValueTable(MobWrapper[].class, _sub_path + _mobs_json);
	    }
	    else if (filename.equals(_nodes_json)) {
			_node_wrapper_array = createDefaultValueTable(NodeWrapper[].class, _sub_path + _nodes_json);
	    }
	    else if (filename.equals(_playlists_json)) {
			_playlist_wrapper_array = createDefaultValueTable(PlaylistWrapper[].class, _sub_path + _playlists_json);
	    }
	    else if (filename.equals(_purchases_json)) {
			_purchase_wrapper_array = createDefaultValueTable(PurchaseWrapper[].class, _sub_path + _purchases_json);
	    }
	    else if (filename.equals(_skills_json)) {
			_skill_wrapper_array = createDefaultValueTable(SkillWrapper[].class, _sub_path + _skills_json);
			addSkillToCharacter();
	    }
	    else if (filename.equals(_tutorials_json)) {
			_tutorial_wrapper_array = createDefaultValueTable(TutorialWrapper[].class, _sub_path + _tutorials_json);
	    }
	    else if (filename.equals(_mob_skills)) {
			_mob_skills_wrapper_array = createDefaultValueTable(MobSkillWrapper[].class, _sub_path + _mob_skills);
	    }
	    else if (filename.equals(_mob_skill_effects)) {
			_mob_skills_effect_wrapper_array = createDefaultValueTable(MobSkillEffectWrapper[].class, _sub_path + _mob_skill_effects);
	    }
	    else {
	    	reload = false;
	    }
		
		return reload;
	}
}
