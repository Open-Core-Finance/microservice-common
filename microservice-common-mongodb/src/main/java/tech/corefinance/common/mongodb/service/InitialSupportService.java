package tech.corefinance.common.mongodb.service;

import org.slf4j.LoggerFactory;
import tech.corefinance.common.config.InitDataConfigurations;
import tech.corefinance.common.context.ApplicationContextHolder;
import tech.corefinance.common.util.CoreFinanceUtil;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public interface InitialSupportService {

    /**
     * List initialize data name configured in class InitDataConfigurations#dataRegex.
     * @return List name supported.
     */
    Map<String, LocalResourceEntityInitializer<? extends Object>> getListInitialNamesSupported();

    default Map<String, Object> initializationDefaultData() throws IOException {
        var log = LoggerFactory.getLogger(getClass());
        var context = ApplicationContextHolder.getInstance().getApplicationContext();
        var config = context.getBean(InitDataConfigurations.class);
        var coreFinanceUtil = context.getBean(CoreFinanceUtil.class);
        var nameSeparator = config.getNameSeparator();
        var versionSeparator = config.getVersionSeparator();
        var result = new LinkedHashMap<String, Object>();
        var listSupportedNames = getListInitialNamesSupported().entrySet();
        for (var entry : config.getDataRegex().entrySet()) {
            var name = entry.getKey();
            var fileNameRegex = entry.getValue();
            var resources = coreFinanceUtil.getResources(fileNameRegex.getFileNameRegex(), nameSeparator, versionSeparator);
            for (var supportedConfig : listSupportedNames) {
                var supportedName = supportedConfig.getKey();
                var initializer = supportedConfig.getValue();
                if (Objects.equals(name, supportedName)) {
                    log.info("Supported [{}]", name);
                    result.put(name, initializer.initializeEntities(resources, fileNameRegex.isReplaceIfExisted()));
                    break;
                }
            }
        }
        // Return
        return result;
    }
}
