 # Compiler
JAVAC = javac
JAVA = java

# Directories
SRC_DIR = src/main/java
BIN_DIR = target
MAIN_CLASS = ui.POOphonia
JAR_NAME = POOphonia.jar
ARCHIVE_NAME = TP1_Member1_Member2.tar.gz

# Find all Java files
SOURCES := $(shell find $(SRC_DIR) -name "*.java")
CLASSES := $(SOURCES:$(SRC_DIR)/%.java=$(BIN_DIR)/%.class)

# Default target: Compile everything
all: compile

# Compile all Java files
compile:
	@mkdir -p $(BIN_DIR)
	$(JAVAC) -d $(BIN_DIR) $(SOURCES)

# Run the application
run: compile
	$(JAVA) -cp $(BIN_DIR) $(MAIN_CLASS)

# Create an executable JAR
jar: compile
	@echo "Packaging $(JAR_NAME)..."
	@mkdir -p $(BIN_DIR)
	jar cfe $(BIN_DIR)/$(JAR_NAME) $(MAIN_CLASS) -C $(BIN_DIR) .

# Clean compiled files
clean:
	rm -rf $(BIN_DIR)

# Archive only .java files while preserving the directory structure
archive:
	@echo "Archiving Java source files into $(ARCHIVE_NAME)..."
	tar -czf $(ARCHIVE_NAME) $(shell find $(SRC_DIR) -type f -name "*.java")

# Help command
help:
	@echo "Available commands:"
	@echo "  make compile  - Compile the Java files"
	@echo "  make run      - Run the application"
	@echo "  make jar      - Create an executable JAR file"
	@echo "  make clean    - Remove compiled files"
	@echo "  make archive  - Archive the Java source files into a .tar.gz file"
	@echo "  make help     - Show available commands"
