import { AccessControl } from "src/app/classes/Permission";

export const environment = {
    production: true,
    apiUrlRoot: '',
    apiUrl: {
        userprofile: '', organization: '', commonRole: '', common: '', authentication: '', productService: '',
        currency: '', holiday: '', branch: '', productCategory: '', exchangeRate: '', rateSource: '', rate: '',
        depositProduct: '', productType: '', glProduct: '', cryptoProduct: '', loanProduct: '', withdrawalChannel: '',
        user: '', glAccountService: '', glAccount: '', customerService: '', individualCustomer: '', corporateCustomer: '',
        depositAccountService: '', loanAccountService: '', depositAccount: '', loanAccount: '', goecodeService: '',
        region: '', subRegion: '', country: '', state: '', city: '', cryptoAccount: '', paymentService: '',
        internalFundTransfer: ''
    },
    apiPrefix: {
        userLogin: '/login',
        refreshToken: '/refresh-token',
        resourceActions: '/resource-actions',
        permissions: "/permissions",
        permissionByRoles: "/permissions/load-by-commonRoles",
        permissionOverrdieByRoles: "/permissions/override-by-commonRoles/"
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
        roleManagement: "commonRole-management",
        depositAccounts: "deposit-accounts",
        loanAccounts: "loan-accounts",
        glAccounts: "gl-accounts",
        cryptoAccounts: "crypto-accounts",
        corporateCustomer: "corporate-customers",
        individualCustomer: "individual-customers",
        region: "regions",
        subRegion: "sub-regions",
        country: "Countries",
        state: "States",
        city: "Cities",
        fundTransfer: "fund-transfer"
    },
    appVersion: "1.0.0-SNAPSHOT",
    appClientId: "corefinance-ADMIN-WEB",
    pageSize: 25,
    pageSizeOptions: [25, 50, 100],
    loginRefreshInterval: 15 * 60 * 1000,
    defaultPermissionIfNull: AccessControl.ALLOWED
};

export function rebuildEnvironment() {
    // Userprofile
    environment.apiUrl.authentication = environment.apiUrl.userprofile + "/authentication";
    environment.apiUrl.user = environment.apiUrl.userprofile + "/userprofiles";
    environment.apiUrl.commonRole = environment.apiUrl.userprofile + "/commonRoles";
    // Product
    environment.apiUrl.organization = environment.apiUrl.productService + "/organizations";
    environment.apiUrl.currency = environment.apiUrl.productService + "/currencies";
    environment.apiUrl.holiday = environment.apiUrl.productService + "/holidays";
    environment.apiUrl.branch = environment.apiUrl.productService + "/branches";
    environment.apiUrl.branch = environment.apiUrl.productService + "/branches";
    environment.apiUrl.productCategory = environment.apiUrl.productService + "/product-categories"
    environment.apiUrl.exchangeRate = environment.apiUrl.productService + "/exchange-rates"
    environment.apiUrl.rateSource = environment.apiUrl.productService + "/rate-sources"
    environment.apiUrl.rate = environment.apiUrl.productService + "/rates"
    environment.apiUrl.depositProduct = environment.apiUrl.productService + "/deposit-products"
    environment.apiUrl.productType = environment.apiUrl.productService + "/product-types"
    environment.apiUrl.glProduct = environment.apiUrl.productService + "/gl-products"
    environment.apiUrl.cryptoProduct = environment.apiUrl.productService + "/crypto-products"
    environment.apiUrl.loanProduct = environment.apiUrl.productService + "/loan-products"
    environment.apiUrl.withdrawalChannel = environment.apiUrl.productService + "/withdrawal-channels"
    // GL Account
    environment.apiUrl.glAccount = environment.apiUrl.glAccountService + "/gl-accounts";
    // Deposit Account
    environment.apiUrl.depositAccount = environment.apiUrl.depositAccountService + "/deposit-accounts";
    environment.apiUrl.cryptoAccount = environment.apiUrl.depositAccountService + "/crypto-accounts";
    // Loan Account
    environment.apiUrl.loanAccount = environment.apiUrl.loanAccountService + "/loan-accounts";
    // Customer
    environment.apiUrl.individualCustomer = environment.apiUrl.customerService + "/individual-customers";
    environment.apiUrl.corporateCustomer = environment.apiUrl.customerService + "/corporate-customers";
    // Geocode
    environment.apiUrl.region = environment.apiUrl.goecodeService + "/regions";
    environment.apiUrl.subRegion = environment.apiUrl.goecodeService + "/sub-regions";
    environment.apiUrl.country = environment.apiUrl.goecodeService + "/countries";
    environment.apiUrl.state = environment.apiUrl.goecodeService + "/states";
    environment.apiUrl.city = environment.apiUrl.goecodeService + "/cities";
    // Payment
    environment.apiUrl.internalFundTransfer = environment.apiUrl.paymentService + "/internal-fund-transfers";
}
