custom_build(
    ref='dispatcher-service',
    command='gradlew.bat bootBuildImage --imageName "%EXPECTED_REF%"',
    deps=['build.gradle', 'settings.gradle', 'gradlew.bat', 'gradle', 'src']
)

k8s_yaml(kustomize('k8s'))

k8s_resource(
    'dispatcher-service',
    port_forwards=[9003]
)