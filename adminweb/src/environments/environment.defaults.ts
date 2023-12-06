export const environment = {
    production: true,
    apiUrlRoot: '',
    apiUrl: {
        userprofile: '', organization: '', role: '', common: '', authentication: '', product: '',
        currency: ''
    },
    apiPrefix: {
        userLogin: '/login',
        refreshToken: '/refresh-token'
    },
    frontEndUrl: {
        login: "login",
        organizations: "organizations",
        curencies: "curencies",
        settings: "settings"
    },
    appVersion: "1.0.0-SNAPSHOT",
    appClientId: "corefinance-ADMIN-WEB",
    pageSize: 25,
    pageSizeOptions: [25, 50, 100],
    loginRefreshInterval: 15 * 60 * 1000,
};
