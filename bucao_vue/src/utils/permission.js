import router from "@/router";
import Layout from "@/Layout/Layout";

// 注意：这个文件是设置动态路由的
// permissions是一个资源的数组
export function activeRouter() {
    const userStr = sessionStorage.getItem("user_info")
    if (userStr) {
        const user = JSON.parse(userStr)
        let root = {
            path: '/',
            name: 'Layout',
            component: Layout,
            redirect: "/home",
            children: []
        }
        console.log(user)
        user.permissions.forEach(p => {
            let obj = {
                path: p.path,
                name: p.name,
                component: () => import("@/views/" + p.name)
            };
            root.children.push(obj)
        })
        if (router) {
            router.addRoute(root)
        }
    }
}


