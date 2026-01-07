import os
import re

def fix_file(filepath):
    try:
        with open(filepath, 'r', encoding='utf-8') as f:
            content = f.read()

        # 1. Vorläufige Bereinigung der mehrfachen Kommentare am Anfang
        new_content = re.sub(r'<!--+', '<!--', content)
        
        # Das Zielformat ist:
        # <!--
        # <properties> ... </properties>
        # <properties> ... </properties>
        # <properties> ... </properties>
        # -->
        # <properties> ... </properties> (Derby)
        
        # Wir suchen den gesamten Block zwischen dem ersten <properties> und </persistence-unit>
        # Wir identifizieren den Derby-Block am Ende.
        
        properties_blocks = re.findall(r'<properties>.*?</properties>', new_content, re.DOTALL)
        
        if len(properties_blocks) < 2:
            return False # Nichts zu tun oder unerwartetes Format
            
        # Wir nehmen an, der letzte Block ist Derby und soll aktiv sein.
        # Alle anderen sollen in EINEM Kommentarblock stehen.
        
        active_block = properties_blocks[-1]
        commented_blocks = properties_blocks[:-1]
        
        # Baue den neuen Inhalt zusammen
        # Wir brauchen den Teil vor dem ersten <properties> Block
        header_match = re.search(r'(.*?)<!--?<properties>', new_content, re.DOTALL)
        if not header_match:
            # Falls es nicht mit <!-- anfängt, suchen wir einfach das erste <properties>
            header_match = re.search(r'(.*?)<properties>', new_content, re.DOTALL)
            
        if not header_match:
            return False
            
        header = header_match.group(1).rstrip()
        
        # Wir brauchen den Teil nach dem letzten </properties> Block
        footer_match = re.search(r'.*</properties>(.*)', new_content, re.DOTALL)
        footer = footer_match.group(1) if footer_match else ""
        
        # Zusammenbau
        result = header + "\n\t\t<!--"
        for block in commented_blocks:
            # Bereinige den Block von eventuellen Resten von Kommentaren innerhalb
            clean_block = block.replace('<!--', '').replace('-->', '')
            result += "\n\t\t" + clean_block
        
        result += "\n\t\t-->\n\t\t" + active_block + footer
        
        # Normiere Whitespace am Ende der Zeilen und doppelte Leerzeilen
        result = re.sub(r' +$', '', result, flags=re.M)

        if result != content:
            with open(filepath, 'w', encoding='utf-8') as f:
                f.write(result)
            return True
    except Exception as e:
        print(f"Error processing {filepath}: {e}")
    return False

# Alle persistence.xml Dateien finden
for root, dirs, files in os.walk('.'):
    # Überspringe 'target' Verzeichnisse, da diese generiert sind
    if 'target' in dirs:
        dirs.remove('target')
    for file in files:
        if file == 'persistence.xml':
            path = os.path.join(root, file)
            if fix_file(path):
                print(f"Fixed: {path}")
