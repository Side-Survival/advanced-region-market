package net.alex9849.arm.regionkind;

import net.alex9849.arm.regions.Region;
import net.alex9849.arm.util.YamlFileManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RegionKindManager extends YamlFileManager<RegionKind> {

    public RegionKindManager(File savepath) {
        super(savepath);
    }

    @Override
    public List<RegionKind> loadSavedObjects(YamlConfiguration yamlConfiguration) {
        List<RegionKind> regionKindList = new ArrayList<>();

        yamlConfiguration.options().copyDefaults(true);

        if(yamlConfiguration.get("DefaultRegionKind") != null) {
            ConfigurationSection defaultRkConfig = yamlConfiguration.getConfigurationSection("DefaultRegionKind");
            updateDefaults(defaultRkConfig);
            RegionKind.DEFAULT = RegionKind.parse(defaultRkConfig, "Default");
        }

        if(yamlConfiguration.get("SubregionRegionKind") != null) {
            ConfigurationSection subregionRkConfig = yamlConfiguration.getConfigurationSection("SubregionRegionKind");
            updateDefaults(subregionRkConfig);
            RegionKind.SUBREGION = RegionKind.parse(subregionRkConfig, "Subregion");
        }

        if(yamlConfiguration.get("RegionKinds") != null) {
            ConfigurationSection regionKindsSection = yamlConfiguration.getConfigurationSection("RegionKinds");
            List<String> regionKinds = new ArrayList<>(regionKindsSection.getKeys(false));
            if(regionKinds != null) {
                for(String regionKindID : regionKinds) {
                    if(regionKindsSection.get(regionKindID) != null) {
                        ConfigurationSection rkConfSection = regionKindsSection.getConfigurationSection(regionKindID);
                        if(rkConfSection != null) {
                            updateDefaults(rkConfSection);
                            regionKindList.add(RegionKind.parse(rkConfSection, regionKindID));
                        }
                    }
                }
            }
        }
        this.saveFile();
        yamlConfiguration.options().copyDefaults(false);
        return regionKindList;
    }

    @Override
    public void saveObjectToYamlObject(RegionKind regionKind, YamlConfiguration yamlConfiguration) {
        yamlConfiguration.set("RegionKinds." + regionKind.getName(), regionKind.toConfigureationSection());
    }

    @Override
    public void writeStaticSettings(YamlConfiguration yamlConfiguration) {
        yamlConfiguration.set("DefaultRegionKind", RegionKind.DEFAULT.toConfigureationSection());
        yamlConfiguration.set("SubregionRegionKind", RegionKind.SUBREGION.toConfigureationSection());
    }

    public List<String> completeTabRegionKinds(String arg, String returnPrefix) {
        List<String> returnme = new ArrayList<>();

        List<RegionKind> regionKinds = this.getObjectListCopy();

        for (RegionKind regionkind : regionKinds) {
            if ((returnPrefix + regionkind.getName()).toLowerCase().startsWith(arg)) {
                returnme.add(returnPrefix + regionkind.getName());
            }
        }
        if ((returnPrefix + "default").startsWith(arg)) {
            returnme.add(returnPrefix + "default");
        }
        if ((returnPrefix + "subregion").startsWith(arg)) {
            returnme.add(returnPrefix + "subregion");
        }

        return returnme;
    }

    public boolean kindExists(String kind){

        List<RegionKind> regionKinds = this.getObjectListCopy();
        for(RegionKind regionKind : regionKinds) {
            if(regionKind.getName().equalsIgnoreCase(kind)){
                return true;
            }
        }

        if(kind.equalsIgnoreCase("default")) {
            return true;
        }
        if(kind.equalsIgnoreCase(RegionKind.DEFAULT.getDisplayName())){
            return true;
        }
        if(kind.equalsIgnoreCase("subregion")) {
            return true;
        }
        if(kind.equalsIgnoreCase(RegionKind.SUBREGION.getDisplayName())){
            return true;
        }
        return false;
    }

    public RegionKind getRegionKind(String name){

        List<RegionKind> regionKinds = this.getObjectListCopy();
        for(RegionKind regionKind : regionKinds) {
            if(regionKind.getName().equalsIgnoreCase(name)){
                return regionKind;
            }
        }

        if(name.equalsIgnoreCase("default") || name.equalsIgnoreCase(RegionKind.DEFAULT.getDisplayName())){
            return RegionKind.DEFAULT;
        }
        if(name.equalsIgnoreCase("subregion") || name.equalsIgnoreCase(RegionKind.SUBREGION.getDisplayName())){
            return RegionKind.SUBREGION;
        }
        return null;
    }

    private void updateDefaults(ConfigurationSection section) {
        section.addDefault("item", "RED_BED");
        section.addDefault("displayName", "Default Displayname");
        section.addDefault("displayInLimits", true);
        section.addDefault("displayInGUI", true);
        section.addDefault("paypackPercentage", 0d);
        section.addDefault("lore", new ArrayList<String>());
    }

    public List<String> tabCompleteRegionKind(String arg) {
        List<RegionKind> regionKinds = this.getObjectListCopy();
        List<String> returnme = new ArrayList<>();

        for(RegionKind regionKind : regionKinds) {
            if(regionKind.getName().equalsIgnoreCase(arg)) {
                returnme.add(regionKind.getName());
            }
        }
        return returnme;
    }
}