package cn.claycoffee.AsteroidBelt;

import cn.claycoffee.ClayTech.ClayTech;
import cn.claycoffee.ClayTech.api.Planet;
import cn.claycoffee.ClayTech.utils.Utils;
import com.narcissu14.spacetech.generator.SpaceGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStreamReader;
import java.util.List;

public class AsteroidBelt extends JavaPlugin {
    public static String locale;
    public static DataYML currentLangYML;
    public static FileConfiguration currentLang;
    private static String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",")
            .split(",")[3];
    private static boolean is115 = true;
    private static boolean compatible = true;
    public static AsteroidBelt instance;

    @SuppressWarnings({"static-access", "unused"})
    public void onEnable() {
        instance = this;
        // boolean habitable, int gravity, int distance, int harmlevel, boolean cold
        Utils.info("Launching... By ClayCofee & Narcissu14");

        this.saveDefaultConfig();
        FileConfiguration config = this.getConfig();
        locale = config.getString("Locale");
        if (locale == null)
            locale = "en-US";
        currentLangYML = new DataYML(locale + ".yml");
        currentLangYML.saveCDefaultConfig();
        currentLangYML.reloadCustomConfig();
        currentLang = currentLangYML.getCustomConfig();
        try {
            FileConfiguration current = YamlConfiguration
                    .loadConfiguration(new InputStreamReader(this.getResource(locale + ".yml"), "UTF8"));
            for (String select : current.getKeys(false)) {
                if (Utils.sectionKeyToList(current.getConfigurationSection(select)).get(0).equalsIgnoreCase("null")
                        || current.getConfigurationSection(select).getKeys(false) == null) {
                    currentLang.createPath(current.getConfigurationSection(select), select);
                    currentLangYML.saveCustomConfig();
                    continue;
                } else {
                    List<String> found = Utils.sectionKeyToList(current.getConfigurationSection(select));
                    List<String> found2 = Utils.sectionKeyToList(currentLang.getConfigurationSection(select));
                    for (String each : found) {
                        if (!Utils.ExitsInListL(each, found2)) {
                            try {
                                if (current.isString(select + "." + each)) {
                                    currentLang.set(select + "." + each, current.getString(select + "." + each));
                                }
                                if (current.isInt(select + "." + each)) {
                                    currentLang.set(select + "." + each, current.getInt(select + "." + each));
                                }
                                if (current.isList(select + "." + each)) {
                                    currentLang.set(select + "." + each, current.getList(select + "." + each));
                                }
                            } catch (Exception e) {
                                Utils.info(
                                        "§cThere is an error when reading the language file.Replacing the new language file..");
                                currentLang = current;
                                currentLangYML.saveCustomConfig();
                                currentLangYML.reloadCustomConfig();
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        } catch (Exception e2) {
            Utils.info("§cThere is an error when reading the language file.Replacing the new language file..");
            e2.printStackTrace();
        }
        currentLangYML.saveCustomConfig();
        currentLangYML.reloadCustomConfig();
        switch (version) {
            case "v1_17_R1":
                break;
            case "v1_16_R3":
                break;
            case "v1_16_R2":
                break;
            case "v1_16_R1":
                break;
            case "v1_15_R1":
                break;
            case "v1_14_R1":
                break;
            case "v1_13_R2":
                break;
            case "v1_13_R1":
                break;
            default:
                compatible = false;
                break;
        }
        if (!compatible) {
            Utils.info(Lang.readGeneralText("Not_compatible"));
            this.getServer().getPluginManager().disablePlugin(this);
        }
        if (!is115) {
            Utils.info(Lang.readGeneralText("Before_115"));
        }
        Metrics mt = new Metrics(this, 6894);
        mt.addCustomChart(new Metrics.SimplePie("language", () -> languageCodeToLanguage(locale)));
        mt.addCustomChart(new Metrics.SimplePie("claytech_version", () -> ClayTech.getInstance().getPluginVersion()));

        Planet AsteroidBelt = new Planet("CAsteroidBelt",
                Utils.newItemD(Material.COBBLESTONE, Lang.readPlanetsText("AsteroidBelt")), new SpaceGenerator(),
                Environment.THE_END, false, 6, 30, 1, true);
        AsteroidBelt.register();

        this.getServer().getPluginManager().registerEvents(new BeltListener(), this);
    }

    private String languageCodeToLanguage(String code) {
        switch (code.toUpperCase()) {
            case "ZH-CN":
                return "Simplified Chinese";
            case "ZH-TW":
                return "Traditional Chinese";
            case "EN-US":
                return "English(US)";
            case "EN-UK":
                return "English(UK)";
            default:
                return code;
        }
    }
}
