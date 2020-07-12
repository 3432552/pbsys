package com.hzy.manager.util;

import com.hzy.manager.vo.Tree;
import com.hzy.manager.vo.router.VueRouter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeUtils {
    protected TreeUtils() {

    }

    //构建部门树
    public static <T> Tree<T> build(List<Tree<T>> nodes) {
        if (nodes == null) {
            return null;
        }
        List<Tree<T>> topNodes = new ArrayList<>();
        nodes.forEach(children -> {
            //证明它是父节点
            String pid = children.getParentId();
            if (pid == null || "0".equals(pid)) {
                topNodes.add(children);
                return;
            }
            //把子类放在children对象里
            for (Tree<T> parent : nodes) {
                String id = parent.getId();
                if (id != null && id.equals(pid)) {
                    parent.getChildren().add(children);
                    children.setHasParent(true);
                    parent.setChildren(true);
                    return;
                }
            }
        });
        Tree<T> root = new Tree<>();
        root.setId("0");
        root.setParentId("");
        root.setHasParent(false);
        root.setChildren(true);
        root.setChecked(true);
        root.setChildren(topNodes);
        root.setText("根节点");
        Map<String, Object> state = new HashMap<>(16);
        state.put("opened", true);
        root.setState(state);
        return root;
    }

    /**
     * 构造前端路由
     *
     * @param routes routes
     * @param <T>    T
     * @return
     */
    public static <T> List<VueRouter<T>> buildVueRouter(List<VueRouter<T>> routes) {
        if (routes == null) {
            return null;
        }
        //父节点
        List<VueRouter<T>> topRoutes = new ArrayList<>();
        routes.forEach(route -> {
            String parentId = route.getParentId();
            if (parentId == null || "0".equals(parentId)) {
                topRoutes.add(route);
                return;
            }
            for (VueRouter<T> parent : routes) {
                String id = parent.getId();
                if (id != null && id.equals(parentId)) {
                    if (parent.getChildren() == null)
                        parent.initChildren();
                    parent.getChildren().add(route);
                    parent.setHasChildren(true);
                    route.setHasParent(true);
                    parent.setHasParent(true);
                    return;
                }
            }
        });
        List<VueRouter<T>> list = new ArrayList<>();
        VueRouter<T> root = new VueRouter<>();
        root.setName("主页");
        root.setComponent("MenuView");
        root.setIcon("none");
        root.setPath("/");
        root.setRedirect("/home");
        root.setChildren(topRoutes);
        list.add(root);

        root = new VueRouter<>();
        root.setName("404");
        root.setComponent("error/404");
        root.setPath("*");
        list.add(root);

        return list;
    }
}