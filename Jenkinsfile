@Library('cicd-pipeline') _

Map config = [
        buildFlow    : [
                PULL_REQUEST: ["Build", "Quality Gate"],
                DEVELOPMENT : ["Build"],
                RELEASE     : ["Build", "Quality Gate", "Publish"],
        ],
        branchMatcher: [RELEASE: ['master'], DEVELOPMENT: ['^(?!master$).*$']],
        // QMA Configurations
        qma          : [configFile: 'quality-config.yaml'],
        build        : [
                image    : 'gradle:jdk8',
                cmd      : './gradlew clean build',
                artifacts: ['build/'],
        ],

        postDeploy   : [
                cmd: "echo \"s'all good!\"",
        ],
]

jarLibraryPipeline(config)
