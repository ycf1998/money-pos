export default {
    "GET_default": {
        code: 200,
        data: {
            current: 1,
            size: 10,
            pages: 1,
            total: 3,
            records: []
        },
    },
    "GET_/auth/router": {
        code: 200,
        data: [{
            "path": "system",
            "meta": {
                "title": "系统管理",
                "icon": "sys-manage"
            },
            "hidden": false,
            "iframe": false,
            "component": "Layout",
            "children": [
                {
                    "path": "user",
                    "name": "User",
                    "meta": {
                        "title": "用户管理",
                        "icon": "sys-user"
                    },
                    "hidden": false,
                    "iframe": false,
                    "component": "system/user/index"
                },
                {
                    "path": "role",
                    "name": "Role",
                    "meta": {
                        "title": "角色管理",
                        "icon": "sys-role"
                    },
                    "hidden": false,
                    "iframe": false,
                    "component": "system/role/index"
                },
                {
                    "path": "permission",
                    "name": "Permission",
                    "meta": {
                        "title": "权限管理",
                        "icon": "sys-permission"
                    },
                    "hidden": false,
                    "iframe": false,
                    "component": "system/permission/index"
                },
                {
                    "path": "dict",
                    "name": "Dict",
                    "meta": {
                        "title": "字典管理",
                        "icon": "sys-dict"
                    },
                    "hidden": false,
                    "iframe": false,
                    "component": "system/dict/index"
                },
                {
                    "path": "tenant",
                    "name": "Tenant",
                    "meta": {
                        "title": "租户管理",
                        "icon": "sys-tenant"
                    },
                    "hidden": false,
                    "iframe": false,
                    "component": "system/tenant/index"
                }
            ]
        }]
    },
    "GET_/auth/own": {
        code: 200,
        data: {
            info: {avatar: 'https://7up.pics/images/2023/10/21/batman.png'},
            roles: [{level: 0}],
            permissions: []
        }
    }
}