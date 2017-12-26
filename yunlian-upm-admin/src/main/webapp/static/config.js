var yuiConfig = require('mtfe_yui-config');

var cosGroups = [
    {
        name: 'cos.core',
        prefix: 'cos-',
        path: 'node_modules/mtfe_cos-core/'
    },
    {
        name: 'fecore',
        prefix: [ 'mt-', 'w-', 'p-', 'e-', 'uix-' ],
        path: 'node_modules/mtfe_fe.core/'
    },
    {
        name: 'cos.ui',
        prefix: 'ui-',
        path: 'node_modules/mtfe_cos-ui/'
    },
    {
        name: 'track',
        prefix: 'track-',
        path: 'node_modules/mtfe_cos_track/'
    },
    {
        name: 'mtupm',
        prefix: 'upm-',
        path: ''
    },
    {
        name: 'list',
        prefix: 'list-',
        path: 'node_modules/mtfe_upm_list'
    }
];
yuiConfig.addCosGroup(cosGroups);
