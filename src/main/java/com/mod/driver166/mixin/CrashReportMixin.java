package com.mod.driver166.mixin;

import com.google.common.collect.Lists;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
<<<<<<< Updated upstream
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.Person;
=======
import net.fabricmc.loader.api.metadata.*;
>>>>>>> Stashed changes
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

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
    public abstract CrashReportSection getSystemDetailsSection();

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
            return new TranslatableText(translationKeys[(int)(Util.getMeasuringTimeNano() % (long)translationKeys.length)]).getString();
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
<<<<<<< Updated upstream
    private void generate_advertise(StringBuilder stringBuilder){
=======
    private void generate_advise(StringBuilder stringBuilder){
>>>>>>> Stashed changes
        stringBuilder.append("\n").append(new TranslatableText("crash.better-crash-reports.adv").getString()).append("\n");
        String[] lines = getCauseAsString().split("\n");
        try {
            if (lines[0].contains("provided by")) {
                if (mods_contains_modid(lines[0].split("provided by")[1].split(" at")[0].split("'")[1])) {
                    String mod_id = lines[0].split("provided by")[1].split(" at")[0].split("'")[1];
                    ModMetadata metadata = getModMetadata(mod_id);
<<<<<<< Updated upstream
                    stringBuilder.append(new TranslatableText("crash.better-crash-reports.adv_a", getModName(lines[0].split("provided by")[1].split(" at")[0].split("'")[1])).getString());
                    stringBuilder.append("\n");
                    stringBuilder.append("--- ").append(new TranslatableText("crash.better-crash-reports.mod_info").getString()).append(" ---\n");
                    stringBuilder.append(new TranslatableText("crash.better-crash-reports.mod_info.name").getString()).append(getModName(mod_id)).append("\n");
                    stringBuilder.append(new TranslatableText("crash.better-crash-reports.mod_info.mod_id").getString()).append(metadata.getId()).append("\n");
                    stringBuilder.append(new TranslatableText("crash.better-crash-reports.mod_info.version").getString()).append(metadata.getVersion()).append("\n");
                    stringBuilder.append(new TranslatableText("crash.better-crash-reports.mod_info.author").getString()).append(getAuthorInString(metadata)).append("\n");
                    stringBuilder.append(new TranslatableText("crash.better-crash-reports.mod_info.description").getString()).append(metadata.getDescription());
                }
            } else if (lines[0].contains("Manually triggered debug crash") || lines[1].contains("pollDebugCrash")) {
=======
                    stringBuilder.append(new TranslatableText(advises[(int)(Util.getMeasuringTimeNano() % (long)advises.length)], getModName(lines[0].split("provided by")[1].split(" at")[0].split("'")[1])).getString());
                    stringBuilder.append("\n");
                    if (metadata != null) {
                        stringBuilder.append("--- ").append(new TranslatableText("crash.better-crash-reports.mod_info").getString()).append(" ---\n");
                        stringBuilder.append(new TranslatableText("crash.better-crash-reports.mod_info.name").getString()).append(getModName(mod_id)).append("\n");
                        stringBuilder.append(new TranslatableText("crash.better-crash-reports.mod_info.mod_id").getString()).append(metadata.getId()).append("\n");
                        stringBuilder.append(new TranslatableText("crash.better-crash-reports.mod_info.version").getString()).append(metadata.getVersion()).append("\n");
                        stringBuilder.append(new TranslatableText("crash.better-crash-reports.mod_info.author").getString()).append(getAuthorInString(metadata)).append("\n");
                        stringBuilder.append(new TranslatableText("crash.better-crash-reports.mod_info.description").getString()).append(metadata.getDescription());
                    }
                }
            } else if (lines[0].contains("Manually triggered debug crash")) {
>>>>>>> Stashed changes
                stringBuilder.append(new TranslatableText("crash.better-crash-reports.adv_b").getString());
            } else if (mods_contains_modid(lines[1].split("\\$")[lines[1].split("\\$").length - 2])) {
                String mod_id = lines[1].split("\\$")[lines[1].split("\\$").length - 2];
                ModMetadata metadata = getModMetadata(mod_id);
<<<<<<< Updated upstream
                stringBuilder.append(new TranslatableText("crash.better-crash-reports.adv_a", getModName(lines[1].split("\\$")[lines[1].split("\\$").length - 2])).getString());
                stringBuilder.append("\n");stringBuilder.append("--- ").append(new TranslatableText("crash.better-crash-reports.mod_info").getString()).append(" ---\n");
                stringBuilder.append(new TranslatableText("crash.better-crash-reports.mod_info.name").getString()).append(getModName(mod_id)).append("\n");
                stringBuilder.append(new TranslatableText("crash.better-crash-reports.mod_info.mod_id").getString()).append(metadata.getId()).append("\n");
                stringBuilder.append(new TranslatableText("crash.better-crash-reports.mod_info.version").getString()).append(metadata.getVersion()).append("\n");
                stringBuilder.append(new TranslatableText("crash.better-crash-reports.mod_info.author").getString()).append(getAuthorInString(metadata)).append("\n");
                stringBuilder.append(new TranslatableText("crash.better-crash-reports.mod_info.description").getString()).append(metadata.getDescription());
=======
                Random random = new Random();
                stringBuilder.append(new TranslatableText(advises[random.nextInt(5)], getModName(mod_id)).getString());
                if (metadata != null) {
                    stringBuilder.append("\n");
                    stringBuilder.append("--- ").append(new TranslatableText("crash.better-crash-reports.mod_info").getString()).append(" ---\n");
                    stringBuilder.append(new TranslatableText("crash.better-crash-reports.mod_info.name").getString()).append(getModName(mod_id)).append("\n");
                    stringBuilder.append(new TranslatableText("crash.better-crash-reports.mod_info.mod_id").getString()).append(metadata.getId()).append("\n");
                    stringBuilder.append(new TranslatableText("crash.better-crash-reports.mod_info.version").getString()).append(metadata.getVersion()).append("\n");
                    stringBuilder.append(new TranslatableText("crash.better-crash-reports.mod_info.author").getString()).append(getAuthorInString(metadata)).append("\n");
                    stringBuilder.append(new TranslatableText("crash.better-crash-reports.mod_info.description").getString()).append(metadata.getDescription());
                }
>>>>>>> Stashed changes
            } else {
                stringBuilder.append(new TranslatableText("crash.better-crash-reports.unknown").getString());
            }
        } catch (IndexOutOfBoundsException ignored){
        }
    }

    @Inject(at = @At(value = "HEAD"), method = "asString", cancellable = true)
    public void asString(CallbackInfoReturnable<String> cir){
        StringBuilder sb = new StringBuilder();
        sb.append("---- ").append(new TranslatableText("crash.better-crash-reports.title").getString()).append(" ---\n");
        sb.append("// ");
        sb.append(generateWittyComment());
        sb.append("\n\n");
        sb.append(new TranslatableText("crash.better-crash-reports.time").getString());
        sb.append(DATE_TIME_FORMATTER.format(ZonedDateTime.now()));
        sb.append("\n");
        sb.append(new TranslatableText("crash.better-crash-reports.description").getString());
        sb.append(getMessage());
        sb.append("\n\n");
        sb.append(new TranslatableText("crash.better-crash-reports.thread").getString()).append(Thread.currentThread().getName()).append("\n");
        sb.append(new TranslatableText("crash.better-crash-reports.stacktrace").getString());
        sb.append("\n");
<<<<<<< Updated upstream
        sb.append(getCauseAsString());
=======
        sb.append(getModifiedCauseAsString());
>>>>>>> Stashed changes
        sb.append("\n\n").append(new TranslatableText("crash.better-crash-reports.mod_used").getString()).append("\n");
        for (ModContainer mod:FabricLoader.getInstance().getAllMods()){
            sb.append("\t- ").append(mod.toString()).append("\n");
        }
        generate_advertise(sb);
        sb.append("\n\n");
        sb.append(new TranslatableText("crash.better-crash-reports.other_infos").getString());
        sb.append("\n");
        for (int i = 0; i < 87; i++) {
            sb.append("-");
        }
        sb.append("\n\n");
        if ((this.stackTrace == null || this.stackTrace.length == 0) && !this.otherSections.isEmpty()) {
            this.stackTrace = ArrayUtils.subarray(this.otherSections.get(0).getStackTrace(), 0, 1);
        }

        for(CrashReportSection crashReportSection : this.otherSections) {
            crashReportSection.addStackTrace(sb);
            sb.append("\n\n");
        }

        this.getSystemDetailsSection().addStackTrace(sb);
        cir.setReturnValue(sb.toString());
    }
<<<<<<< Updated upstream
=======
    @Unique
    private String joinListWithSeparator(List<String> list, @Nullable String separator){
        StringBuilder stringBuilder = new StringBuilder();
        for (String element : list){
            stringBuilder.append(element);
            if (!element.equals(list.get(list.size() - 1))){
                stringBuilder.append(separator);
            }
        }
        return stringBuilder.toString();
    }

    @Unique
    private String getModifiedCauseAsString() {
        String cause = getCauseAsString();
        try{
            String[] splitCause = cause.split("\n");
            String cause_string = splitCause[0].split(": ")[0];
            String modified_cause_string = cause_string + (new TranslatableText(cause_string).getString().equals(cause_string) ? "" : "(" + new TranslatableText(cause_string).getString() + ")");
            if (splitCause[0].split(": ").length != 1){
                List<String> causeList = Arrays.asList(splitCause[0].split(": "));
                causeList.set(0, modified_cause_string);
                String causeFirstLine = joinListWithSeparator(causeList, ": ");
                List<String> modifiedSplitCause = Arrays.asList(splitCause);
                modifiedSplitCause.set(0, causeFirstLine);
                cause = joinListWithSeparator(modifiedSplitCause, "\n");
            }
        } catch (IndexOutOfBoundsException ignored){
        }
        return cause;
    }

>>>>>>> Stashed changes

    static {
        DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ROOT);
    }
}
