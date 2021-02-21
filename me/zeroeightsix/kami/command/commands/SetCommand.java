/*
 * Decompiled with CFR 0.151.
 */
package me.zeroeightsix.kami.command.commands;

import java.util.Optional;
import java.util.stream.Collectors;
import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.command.syntax.ChunkBuilder;
import me.zeroeightsix.kami.command.syntax.parsers.ModuleParser;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.module.ModuleManager;
import me.zeroeightsix.kami.setting.Named;
import me.zeroeightsix.kami.setting.Setting;

public class SetCommand
extends Command {
    public SetCommand() {
        super("set", new ChunkBuilder().append("module", true, new ModuleParser()).append("setting", true).append("value", true).build());
    }

    @Override
    public void call(String[] args) {
        if (args[0] == null) {
            Command.sendChatMessage("Please specify a module!");
            return;
        }
        Module m = ModuleManager.getModuleByName(args[0]);
        if (m == null) {
            Command.sendChatMessage("Unknown module &b" + args[0] + "&r!");
            return;
        }
        if (args[1] == null) {
            String settings = String.join((CharSequence)", ", m.settingList.stream().filter(setting -> setting instanceof Named).map(setting -> ((Named)((Object)setting)).getName()).collect(Collectors.toList()));
            if (settings.isEmpty()) {
                Command.sendChatMessage("Module &b" + m.getName() + "&r has no settings.");
            } else {
                Command.sendStringChatMessage(new String[]{"Please specify a setting! Choose one of the following:", settings});
            }
            return;
        }
        Optional<Setting> optionalSetting = m.settingList.stream().filter(setting1 -> setting1 instanceof Named).filter(setting1 -> ((Named)((Object)setting1)).getName().equalsIgnoreCase(args[1])).findFirst();
        if (!optionalSetting.isPresent()) {
            Command.sendChatMessage("Unknown setting &b" + args[1] + "&r in &b" + m.getName() + "&r!");
            return;
        }
        Setting setting2 = optionalSetting.get();
        if (args[2] == null) {
            Command.sendChatMessage("&b" + ((Named)((Object)setting2)).getName() + "&r is a &3" + setting2.getValue().getClass().getSimpleName() + "&r. Its current value is &3" + setting2.getValue());
            return;
        }
        try {
            setting2.setValue(args[2]);
            Command.sendChatMessage("Set &b" + ((Named)((Object)setting2)).getName() + "&r to &3" + args[2] + "&r.");
        }
        catch (Exception e) {
            e.printStackTrace();
            Command.sendChatMessage("Unable to set value! &6" + e.getMessage());
        }
    }
}

