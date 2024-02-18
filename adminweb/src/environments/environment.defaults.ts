import { AccessControl } from "src/app/classes/Permission";

export const environment = {
    production: true,
    apiUrlRoot: '',
    apiUrl: {
        userprofile: '', organization: '', role: '', common: '', authentication: '', product: '',
        currency: '', holiday: '', branch: '', productCategory: '', exchangeRate: '', rateSource: '', rate: '',
        depositProduct: '', productType: '', glProduct: '', cryptoProduct: '', loanProduct: '', withdrawalChannel: '',
        user: ''
    },
    apiPrefix: {
        userLogin: '/login',
        refreshToken: '/refresh-token',
        resourceActions: '/resource-actions',
        permissions: "/permissions",
        permissionByRoles: "/permissions/load-by-roles",
        permissionOverrdieByRoles: "/permissions/override-by-roles/"
    },
    frontEndUrl: {
        login: "login",
        organizations: "organizations",
        currencies: "currencies",
        settings: "settings",
        organizationDetails: "organization-details",
        holidays: "holidays",
        branches: "Branches",
        depositProducts: "deposit-products",
        productCategories: "product-categories",
        productTypes: "product-types",
        loanProducts: "loan-products",
        glProducts: "gl-products",
        cryptoProducts: "crypto-products",
        exchangeRates: "exchange-rates",
        rates: "rates",
        rateSources: "rate-sources",
        userManagement: "user-management",
        roleManagement: "role-management"
    },
    appVersion: "1.0.0-SNAPSHOT",
    appClientId: "corefinance-ADMIN-WEB",
    pageSize: 25,
    pageSizeOptions: [25, 50, 100],
    loginRefreshInterval: 15 * 60 * 1000,
    defaultPermissionIfNull: AccessControl.DENIED
};

export function rebuildEnvironment() {
    // Userprofile
    environment.apiUrl.authentication = environment.apiUrl.userprofile + "/authentication";
    environment.apiUrl.user = environment.apiUrl.userprofile + "/userprofiles";
    environment.apiUrl.role = environment.apiUrl.userprofile + "/roles";
    // Product
    environment.apiUrl.organization = environment.apiUrl.product + "/organizations";
    environment.apiUrl.currency = environment.apiUrl.product + "/currencies";
    environment.apiUrl.holiday = environment.apiUrl.product + "/holidays";
    environment.apiUrl.branch = environment.apiUrl.product + "/branches";
    environment.apiUrl.branch = environment.apiUrl.product + "/branches";
    environment.apiUrl.productCategory = environment.apiUrl.product + "/product-categories"
    environment.apiUrl.exchangeRate = environment.apiUrl.product + "/exchange-rates"
    environment.apiUrl.rateSource = environment.apiUrl.product + "/rate-sources"
    environment.apiUrl.rate = environment.apiUrl.product + "/rates"
    environment.apiUrl.depositProduct = environment.apiUrl.product + "/deposit-products"
    environment.apiUrl.productType = environment.apiUrl.product + "/product-types"
    environment.apiUrl.glProduct = environment.apiUrl.product + "/gl-products"
    environment.apiUrl.cryptoProduct = environment.apiUrl.product + "/crypto-products"
    environment.apiUrl.loanProduct = environment.apiUrl.product + "/loan-products"
    environment.apiUrl.withdrawalChannel = environment.apiUrl.product + "/withdrawal-channels"
}
