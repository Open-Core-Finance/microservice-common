import { environment as defaultEnvironment, rebuildEnvironment } from './environment.defaults';

export const environment = {
    ...defaultEnvironment,
    production: true,
    apiUrlRoot: 'https://corefinance.tech/api',
};

environment.apiUrl.userprofile = "http://userprofile.corefinance.tech";
environment.apiUrl.productService = "http://product.corefinance.tech";
environment.apiUrl.accountService = "http://account.corefinance.tech";

rebuildEnvironment();
