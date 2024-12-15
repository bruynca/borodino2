import json
import re

# Input and output file names
input_file = 'french.txt'
output_file = 'output.json'

# Structure to hold the hierarchy
french_army = {
    "FrenchArmy": {
        "Corp": []
    }
}

current_corp = None
current_division = None

# Read the input file and create JSON structure
with open(input_file, 'r') as file:
    for line in file:
        # Strip any leading/trailing whitespace
        line = line.strip()
        
        # Check for Corps
        if 'Corps' in line:
            corp_name = re.search(r'\S+', line).group()
            current_corp = {
                "name": corp_name,
                "Division": []
            }
            french_army["FrenchArmy"]["Corp"].append(current_corp)
            current_division = None  # Reset when moving to a new Corps
        
        # Check for Division
        if 'Division' in line and current_corp:
            division_name = re.search(r'\S+', line).group()
            current_division = {
                "name": division_name,
                "Brigade": []
            }
            current_corp["Division"].append(current_division)
        
            # Check for Brigade on the same line
            if 'Brigade' in line:
                brigade_name = re.search(r'Brigade\s+(\S+)', line)
                if brigade_name:
                    brigade_name = brigade_name.group(1)
                    current_division["Brigade"].append({
                        "name": brigade_name,
                        "moveFactor": 4
                    })
        
        # Check for Brigade on a separate line
        elif 'Brigade' in line and current_division:
            brigade_name = re.search(r'\S+', line.split('Brigade', 1)[1].strip()).group()
            current_division["Brigade"].append({
                "name": brigade_name,
                "moveFactor": 4
            })

# Write the JSON structure to the file
with open(output_file, 'w', encoding='utf-8') as f:
    json.dump(french_army, f, indent=2)