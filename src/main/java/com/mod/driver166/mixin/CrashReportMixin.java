package com.mod.driver166.mixin;

import com.google.common.collect.Lists;
import com.mod.driver166.BetterCrashReports;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.Person;
import net.minecraft.text.Text;
import net.minecraft.util.SystemDetails;
import net.minecraft.util.Util;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Mixin(CrashReport.class)
public abstract class CrashReportMixin {
    @Shadow
    public abstract String getMessage();
    @Shadow
    public abstract String getCauseAsString();
    @Shadow
    private final List<CrashReportSection> otherSections = Lists.newArrayList();
    @Shadow
    private StackTraceElement[] stackTrace = new StackTraceElement[0];
    @Shadow
    private final SystemDetails systemDetailsSection = new SystemDetails();

    @Unique
    private static final DateTimeFormatter DATE_TIME_FORMATTER;
    @Unique
    private static String generateWittyComment() {
        // 将原来的字符串数组替换为翻译键数组
        String[] translationKeys = new String[]{
                "crash.better-crash-report.comment.0",
                "crash.better-crash-report.comment.1",
                "crash.better-crash-report.comment.2",
                "crash.better-crash-report.comment.3",
                "crash.better-crash-report.comment.4",
                "crash.better-crash-report.comment.5",
                "crash.better-crash-report.comment.6",
                "crash.better-crash-report.comment.7",
                "crash.better-crash-report.comment.8",
                "crash.better-crash-report.comment.9",
                "crash.better-crash-report.comment.10",
                "crash.better-crash-report.comment.11",
                "crash.better-crash-report.comment.12",
                "crash.better-crash-report.comment.13",
                "crash.better-crash-report.comment.14",
                "crash.better-crash-report.comment.15",
                "crash.better-crash-report.comment.16",
                "crash.better-crash-report.comment.17",
                "crash.better-crash-report.comment.18",
                "crash.better-crash-report.comment.19",
                "crash.better-crash-report.comment.20",
                "crash.better-crash-report.comment.21",
                "crash.better-crash-report.comment.22",
                "crash.better-crash-report.comment.23",
                "crash.better-crash-report.comment.24",
                "crash.better-crash-report.comment.25",
                "crash.better-crash-report.comment.26",
                "crash.better-crash-report.comment.27",
                "crash.better-crash-report.comment.28",
                "crash.better-crash-report.comment.29",
                "crash.better-crash-report.comment.30",
                "crash.better-crash-report.comment.31",
                "crash.better-crash-report.comment.32"
        };

        try {
            return Text.translatable(translationKeys[(int)(Util.getMeasuringTimeNano() % (long)translationKeys.length)]).getString();
        } catch (Throwable var2) {
            return "Witty comment unavailable :(";
        }
    }
    @Unique
    public List<String> get_mod_list(){
        List<String> mod_list = new ArrayList<>();
        for (ModContainer mod:FabricLoader.getInstance().getAllMods()){
            mod_list.add(mod.toString().split(" ")[0]);
        }
        return mod_list;
    }
    @Unique
    public boolean mods_contains_modid(String modid){
        List<String> mod_list = get_mod_list();
        return mod_list.contains(modid);
    }
    @Unique
    private static String getModName(String modId) {
        Optional<ModContainer> optionalContainer = FabricLoader.getInstance().getModContainer(modId);
        if (optionalContainer.isPresent()) {
            ModContainer container = optionalContainer.get();
            return container.getMetadata().getName();
        }
        return modId;
    }
    @Unique
    private static @Nullable ModMetadata getModMetadata(String modId) {
        Optional<ModContainer> optionalContainer = FabricLoader.getInstance().getModContainer(modId);
        return optionalContainer.map(ModContainer::getMetadata).orElse(null);
    }
    @Unique
    private static String getAuthorInString(ModMetadata metadata) {
        StringBuilder sb = new StringBuilder();
        for (Person person : metadata.getAuthors()){
            sb.append(person.getName()).append(" ");
        }
        return sb.toString();
    }
    @Unique
    private void generate_advertise(StringBuilder stringBuilder){
        stringBuilder.append("\n").append(Text.translatable("crash.better-crash-reports.adv").getString()).append("\n");
        String[] lines = getCauseAsString().split("\n");
        try {
            if (lines[0].contains("provided by")) {
                if (mods_contains_modid(lines[0].split("provided by")[1].split(" at")[0].split("'")[1])) {
                    String mod_id = lines[0].split("provided by")[1].split(" at")[0].split("'")[1];
                    ModMetadata metadata = getModMetadata(mod_id);
                    stringBuilder.append(Text.translatable("crash.better-crash-reports.adv_a", getModName(lines[0].split("provided by")[1].split(" at")[0].split("'")[1])).getString());
                    stringBuilder.append("\n");
                    stringBuilder.append("--- ").append(Text.translatable("crash.better-crash-reports.mod_info").getString()).append(" ---\n");
                    stringBuilder.append(Text.translatable("crash.better-crash-reports.mod_info.name").getString()).append(getModName(mod_id)).append("\n");
                    stringBuilder.append(Text.translatable("crash.better-crash-reports.mod_info.mod_id").getString()).append(metadata.getId()).append("\n");
                    stringBuilder.append(Text.translatable("crash.better-crash-reports.mod_info.version").getString()).append(metadata.getVersion()).append("\n");
                    stringBuilder.append(Text.translatable("crash.better-crash-reports.mod_info.author").getString()).append(getAuthorInString(metadata)).append("\n");
                    stringBuilder.append(Text.translatable("crash.better-crash-reports.mod_info.description").getString()).append(metadata.getDescription());
                }
            } else if (lines[0].contains("Manually triggered debug crash")) {
                stringBuilder.append(Text.translatable("crash.better-crash-reports.adv_b").getString());
            } else if (mods_contains_modid(lines[1].split("\\$")[lines[1].split("\\$").length - 2])) {
                String mod_id = lines[1].split("\\$")[lines[1].split("\\$").length - 2];
                ModMetadata metadata = getModMetadata(mod_id);
                stringBuilder.append(Text.translatable("crash.better-crash-reports.adv_a", getModName(lines[1].split("\\$")[lines[1].split("\\$").length - 2])).getString());
                stringBuilder.append("\n");stringBuilder.append("--- ").append(Text.translatable("crash.better-crash-reports.mod_info").getString()).append(" ---\n");
                stringBuilder.append(Text.translatable("crash.better-crash-reports.mod_info.name").getString()).append(getModName(mod_id)).append("\n");
                stringBuilder.append(Text.translatable("crash.better-crash-reports.mod_info.mod_id").getString()).append(metadata.getId()).append("\n");
                stringBuilder.append(Text.translatable("crash.better-crash-reports.mod_info.version").getString()).append(metadata.getVersion()).append("\n");
                stringBuilder.append(Text.translatable("crash.better-crash-reports.mod_info.author").getString()).append(getAuthorInString(metadata)).append("\n");
                stringBuilder.append(Text.translatable("crash.better-crash-reports.mod_info.description").getString()).append(metadata.getDescription());
            } else {
                stringBuilder.append(Text.translatable("crash.better-crash-reports.unknown").getString());
            }
        } catch (IndexOutOfBoundsException ignored){
        }
    }

    @Inject(at = @At(value = "HEAD"), method = "asString", cancellable = true)
    public void asString(CallbackInfoReturnable<String> cir){
        StringBuilder sb = new StringBuilder();
        sb.append("---- ").append(Text.translatable("crash.better-crash-reports.title").getString()).append(" ---\n");
        sb.append("// ");
        sb.append(generateWittyComment());
        sb.append("\n\n");
        sb.append(Text.translatable("crash.better-crash-reports.time").getString());
        sb.append(DATE_TIME_FORMATTER.format(ZonedDateTime.now()));
        sb.append("\n");
        sb.append(Text.translatable("crash.better-crash-reports.description").getString());
        sb.append(getMessage());
        sb.append("\n\n");
        sb.append(Text.translatable("crash.better-crash-reports.thread").getString()).append(Thread.currentThread().getName()).append("\n");
        sb.append(Text.translatable("crash.better-crash-reports.stacktrace").getString());
        sb.append("\n");
        sb.append(getCauseAsString());
        sb.append("\n\n").append(Text.translatable("crash.better-crash-reports.mod_used").getString()).append("\n");
        for (ModContainer mod:FabricLoader.getInstance().getAllMods()){
            sb.append("\t- ").append(mod.toString()).append("\n");
        }
        generate_advertise(sb);
        sb.append("\n\n");
        sb.append(Text.translatable("crash.better-crash-reports.other_infos").getString());
        sb.append("\n");
        sb.append("-".repeat(87));
        sb.append("\n\n");
        if ((this.stackTrace == null || this.stackTrace.length == 0) && !this.otherSections.isEmpty()) {
            this.stackTrace = ArrayUtils.subarray(this.otherSections.get(0).getStackTrace(), 0, 1);
        }

        for(CrashReportSection crashReportSection : this.otherSections) {
            crashReportSection.addStackTrace(sb);
            sb.append("\n\n");
        }

        this.systemDetailsSection.writeTo(sb);

        cir.setReturnValue(sb.toString());
    }

    static {
        DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ROOT);
    }
}
