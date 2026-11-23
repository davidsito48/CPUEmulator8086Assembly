# Intel 8086 CPU Emulator (Java 8 & JavaFX)

A visual, step-by-step **Intel 8086 CPU emulator** built with **Java 8** and **JavaFX**. It highlights the active path or component in a CPU block diagram and displays memory changes as each instruction executes, keeping the codebase straightforward so you can read it and see how the hardware model works.

## What It Shows

- **Step-by-Step Execution:** Run one instruction at a time (or step through a program) to observe state changes clearly.
- **CPU Diagram Highlighting:** Visually indicates where execution is happening in the CPU architecture.
- **Memory View:** Shows memory contents and updates as instructions read from or write to memory.
- **Readable Codebase:** Focused, easy-to-follow Java classes separating CPU state/execution from the JavaFX UI.

## Tech Stack

- **Java 8** (JDK 1.8)
- **JavaFX 8** (included in standard Oracle/Zulu JDK 8 distributions, or added via OpenJFX 8)
