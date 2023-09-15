package tech.corefinance.common.service;

import tech.corefinance.common.config.InitDataConfigurations;
import tech.corefinance.common.context.ApplicationContextHolder;
import tech.corefinance.common.model.GenericModel;
import tech.corefinance.common.repository.CommonResourceRepository;
import tech.corefinance.common.util.CoreFinanceUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public interface InitialSupportService<I extends Serializable, T extends GenericModel<I>, R extends CommonResourceRepository<T, I>> {

    /**
     * Get repository.
     * @return Repository object that this service manage.
     */
    R getRepository();

    /**
     * List initialize data name configured in class InitDataConfigurations#dataRegex.
     * @return List name supported.
     */
    Map<String, EntityInitializer<? extends Object>> getListInitialNamesSupported();

    default Map<String, Object> initializationDefaultData() throws IOException {
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
                    result.put(name, initializer.initializeEntities(resources, fileNameRegex.isReplaceIfExisted()));
                    break;
                }
            }
        }
        // Return
        return result;
    }
}
