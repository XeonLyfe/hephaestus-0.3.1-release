//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.TextComponentBase
 */
package me.zeroeightsix.kami.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.zeroeightsix.kami.command.syntax.SyntaxChunk;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import me.zeroeightsix.kami.util.Wrapper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentBase;

public abstract class Command {
    public static Setting<String> commandPrefix = Settings.s("commandPrefix", ".");
    protected String label;
    protected String syntax;
    protected String description;
    protected SyntaxChunk[] syntaxChunks;

    public Command(String label, SyntaxChunk[] syntaxChunks) {
        this.label = label;
        this.syntaxChunks = syntaxChunks;
        this.description = "Descriptionless";
    }

    public static void sendChatMessage(String message) {
        Command.sendRawChatMessage("\u00a7c\u041d\u03b5\u13ae\u043d\u15e9\u03b5\u0455\u01ad\u03c5\u0455\u00a78 ->  \u00a7r" + message);
    }

    public static void sendStringChatMessage(String[] messages) {
        Command.sendChatMessage("");
        for (String s : messages) {
            Command.sendRawChatMessage(s);
        }
    }

    public static void sendRawChatMessage(String message) {
        Wrapper.getPlayer().sendMessage((ITextComponent)new ChatMessage(message));
    }

    public static String getCommandPrefix() {
        return commandPrefix.getValue();
    }

    public static char SECTIONSIGN() {
        return '\u00a7';
    }

    public String getDescription() {
        return this.description;
    }

    protected void setDescription(String description) {
        this.description = description;
    }

    public String getLabel() {
        return this.label;
    }

    public abstract void call(String[] var1);

    public SyntaxChunk[] getSyntaxChunks() {
        return this.syntaxChunks;
    }

    protected SyntaxChunk getSyntaxChunk(String name) {
        for (SyntaxChunk c : this.syntaxChunks) {
            if (!c.getType().equals(name)) continue;
            return c;
        }
        return null;
    }

    public static class ChatMessage
    extends TextComponentBase {
        String text;

        public ChatMessage(String text) {
            Pattern p = Pattern.compile("&[0123456789abcdefrlosmk]");
            Matcher m = p.matcher(text);
            StringBuffer sb = new StringBuffer();
            while (m.find()) {
                String replacement = "\u00a7" + m.group().substring(1);
                m.appendReplacement(sb, replacement);
            }
            m.appendTail(sb);
            this.text = sb.toString();
        }

        public String getUnformattedComponentText() {
            return this.text;
        }

        public ITextComponent createCopy() {
            return new ChatMessage(this.text);
        }
    }
}

