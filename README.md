# Android benchmark

There is a 75% chance I will go to Apple Store tomorrow and get a base model MBA with M1. 
So if you want to know the performance of that machine, I wrote a simple script to test 
it out.

## What this does

This is basically just a script to wrap [Gradle Profiler](https://github.com/gradle/gradle-profiler) 
to run through all the projects in `projects.csv`

## What is the testing condition?

- (Machine spec TBC)
- Charging
- 5 minutes between each projects
- [Running everything through Rosetta 2 with `arch -x86_64`](https://twitter.com/soffes/status/1328925421069471746)

## How to run

### Install basics

1. Homebrew
   Follow the instructure from [their website](https://soffes.blog/homebrew-on-apple-silicon)
2. `kotlin`
   Run `arch -x86_64 brew install kotlin` in Terminal
3. Gradle Profiler
   Run `arch -x86_64 brew install gradle-profiler` in Terminal

### Run

1. Clone this repository
1. Open the repository in Terminal
1. ./run.main.kts
1. Sit back and wait

### Share the result

1. Fork this repo on GitHub
1. Add a new `remote` to the repo
1. `git add data`
1. `git commit -m` + some message
1. Push the result to your fork
1. Now open a Pull Request
