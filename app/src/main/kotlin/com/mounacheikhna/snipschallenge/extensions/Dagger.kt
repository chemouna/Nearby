package com.mounacheikhna.snipschallenge.extensions


fun <T> createComponent(componentClass: Class<T>, vararg dependencies: Any): T {
    val fqn = componentClass.name

    val packageName = componentClass.`package`.name
    // Accounts for inner classes, ie MyApplication$EventComponent
    val simpleName = fqn.substring(packageName.length + 1)
    val generatedName = (packageName + ".Dagger" + simpleName).replace('$', '_')

    try {
        val generatedClass = Class.forName(generatedName)
        val builder = generatedClass.getMethod("builder").invoke(null)

        for (method in builder.javaClass.methods) {
            val params = method.parameterTypes
            if (params.size() == 1) {
                val dependencyClass = params[0]
                for (dependency in dependencies) {
                    if (dependencyClass.isAssignableFrom(dependency.javaClass)) {
                        method.invoke(builder, dependency)
                        break
                    }
                }
            }
        }
        //noinspection unchecked
        return builder.javaClass.getMethod("build").invoke(builder) as T
    } catch (e: Exception) {
        throw RuntimeException(e)
    }

}
