package com.mod.driver166.mixin;

import com.google.common.collect.Lists;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.*;
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
import java.util.*;

@Mixin(CrashReport.class)
public abstract class CrashReportMixin {
    @Shadow
    public abstract String getMessage();
    @Shadow
    private final List<CrashReportSection> otherSections = Lists.newArrayList();
    @Shadow
    private StackTraceElement[] stackTrace = new StackTraceElement[0];
    @Shadow
    public abstract String getCauseAsString();

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
        for (ModContainer mod : FabricLoader.getInstance().getAllMods()){
            // 获取真实的模组ID，而不是从toString解析
            String modId = mod.getMetadata().getId();
            mod_list.add(modId);
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
    private void genAdvA(StringBuilder stringBuilder, String mod_id){
        String[] advises = new String[]{
                "crash.better-crash-reports.adv_a.1",
                "crash.better-crash-reports.adv_a.2",
                "crash.better-crash-reports.adv_a.3",
                "crash.better-crash-reports.adv_a.4",
                "crash.better-crash-reports.adv_a.5"
        };
        ModMetadata metadata = getModMetadata(mod_id);
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
    private void generate_advise(StringBuilder stringBuilder){
        stringBuilder.append("\n").append(new TranslatableText("crash.better-crash-reports.adv").getString()).append("\n");
        String[] lines = getCauseAsString().split("\n");
        try {
            if (lines[0].contains("provided by")) {
                if (mods_contains_modid(lines[0].split("provided by")[1].split(" at")[0].split("'")[1])) {
                    String mod_id = lines[0].split("provided by")[1].split(" at")[0].split("'")[1];
                    genAdvA(stringBuilder, mod_id);
                } else {
                    stringBuilder.append(new TranslatableText("crash.better-crash-reports.unknown").getString());
                }
            } else if (lines[0].contains("Manually triggered debug crash")) {
                stringBuilder.append(new TranslatableText("crash.better-crash-reports.adv_b").getString());
            } else {
                Set<String> possibleMods = new HashSet<>();

                // 方法1：原来的 $ 符号方法
                for (String line : lines) {
                    String mod_id = "";
                    try{
                        mod_id = line.split("\\$")[line.split("\\$").length - 2];
                    } catch (IndexOutOfBoundsException ignored) {}

                    if (!mod_id.isEmpty() && mods_contains_modid(mod_id)) {
                        possibleMods.add(mod_id);
                    }
                }

                // 方法2：直接从类名匹配模组ID
                if (possibleMods.isEmpty()) {
                    for (String line : lines) {
                        if (!line.startsWith("\tat ")) continue;

                        // 提取类名
                        String classPart = line.substring(line.lastIndexOf("at ") + 3);
                        if (classPart.contains("//")) {
                            classPart = classPart.substring(classPart.indexOf("//") + 2);
                        }
                        int parenIndex = classPart.indexOf('(');
                        if (parenIndex > 0) {
                            String fullClassName = classPart.substring(0, parenIndex);
                            int lastDot = fullClassName.lastIndexOf('.');
                            if (lastDot > 0) {
                                String className = fullClassName.substring(0, lastDot);

                                // 检查所有模组ID
                                for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
                                    String modId = mod.getMetadata().getId();
                                    if (!className.startsWith("net.minecraft") &&
                                            !className.startsWith("com.mojang") &&
                                            !className.startsWith("net.fabricmc") &&
                                            !className.startsWith("java.") &&  // 过滤Java类
                                            !className.startsWith("javax.") && // 过滤Java类
                                            !className.startsWith("org.lwjgl") &&
                                            className.contains(modId)) {
                                        possibleMods.add(modId);
                                    }
                                }
                            }
                        }
                    }
                }

                // 方法3：基于包名的启发式匹配（简化版）
                if (possibleMods.isEmpty()) {
                    for (String line : lines) {
                        if (!line.startsWith("\tat ")) continue;

                        String classPart = line.substring(line.lastIndexOf("at ") + 3);
                        if (classPart.contains("//")) {
                            classPart = classPart.substring(classPart.indexOf("//") + 2);
                        }
                        int parenIndex = classPart.indexOf('(');
                        if (parenIndex > 0) {
                            String fullClassName = classPart.substring(0, parenIndex);
                            int lastDot = fullClassName.lastIndexOf('.');
                            if (lastDot > 0) {
                                String className = fullClassName.substring(0, lastDot);
                                String[] parts = className.split("\\.");

                                // 跳过常见的顶级域名
                                int startIdx = 0;
                                if (parts.length > 2 && parts[0].matches("^(com|net|org|io|me)$")) {
                                    startIdx = 1;
                                }

                                // 尝试每个包名部分
                                for (int i = startIdx; i < parts.length; i++) {
                                    String candidate = parts[i];
                                    // 跳过常见的内部包名
                                    if (candidate.matches("^(api|client|common|core|impl|internal|server|util|mixin)$")) {
                                        continue;
                                    }
                                    if (mods_contains_modid(candidate)) {
                                        possibleMods.add(candidate);
                                        break;
                                    }
                                    // 尝试带连字符的版本
                                    String withHyphen = candidate.replace('.', '-');
                                    if (mods_contains_modid(withHyphen)) {
                                        possibleMods.add(withHyphen);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

                // 输出结果
                if (!possibleMods.isEmpty()) {
                    if (possibleMods.size() == 1) {
                        genAdvA(stringBuilder, possibleMods.iterator().next());
                    } else {
                        stringBuilder.append(new TranslatableText("crash.better-crash-reports.multiple_mods").getString()).append("\n");
                        for (String modId : possibleMods) {
                            stringBuilder.append("  - ").append(getModName(modId)).append(" (").append(modId).append(")\n");
                        }
                    }
                } else {
                    stringBuilder.append(new TranslatableText("crash.better-crash-reports.unknown").getString());
                }
            }
        } catch (Exception e) {
            // 确保不会在崩溃报告中再次崩溃
            stringBuilder.append(new TranslatableText("crash.better-crash-reports.unknown").getString());
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
        sb.append(getModifiedCauseAsString());
        sb.append("\n\n").append(new TranslatableText("crash.better-crash-reports.mod_used").getString()).append("\n");
        for (ModContainer mod:FabricLoader.getInstance().getAllMods()){
            sb.append("\t- ").append(mod.toString()).append("\n");
        }
        generate_advise(sb);
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


    static {
        DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ROOT);
    }
}
