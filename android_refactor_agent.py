#!/usr/bin/env python3
"""
Android Project File Refactoring Agent

This script analyzes an Android project and refactors filenames to follow 
standard Android naming conventions. It handles layout files, Java/Kotlin 
classes, drawable files, and menu files while updating all references.

Usage: python3 android_refactor_agent.py [project_path]
"""

import os
import re
import sys
import json
import shutil
from pathlib import Path
from typing import Dict, List, Tuple, Set
from dataclasses import dataclass
from collections import defaultdict

@dataclass
class FileRename:
    """Represents a file rename operation"""
    old_path: str
    new_path: str
    old_name: str
    new_name: str
    file_type: str
    reason: str

@dataclass
class ReferenceUpdate:
    """Represents a reference update operation"""
    file_path: str
    old_reference: str
    new_reference: str
    line_number: int
    context: str

class AndroidRefactorAgent:
    def __init__(self, project_path: str):
        self.project_path = Path(project_path)
        self.app_path = self.project_path / "app"
        self.src_path = self.app_path / "src" / "main"
        self.java_path = self.src_path / "java"
        self.res_path = self.src_path / "res"
        
        # Track all changes
        self.file_renames: List[FileRename] = []
        self.reference_updates: List[ReferenceUpdate] = []
        self.processed_files: Set[str] = set()
        
        # Mapping of old names to new names for reference updates
        self.name_mapping: Dict[str, str] = {}
        
        # Layout file patterns
        self.layout_patterns = {
            'activity': r'^activity_.*\.xml$',
            'fragment': r'^fragment_.*\.xml$',
            'item': r'^item_.*\.xml$',
            'dialog': r'^dialog_.*\.xml$',
            'bottom_sheet': r'^bottom_sheet_.*\.xml$',
            'include': r'^include_.*\.xml$',
            'merge': r'^merge_.*\.xml$'
        }
        
        # Java/Kotlin class patterns
        self.java_patterns = {
            'Activity': r'.*Activity\.(java|kt)$',
            'Fragment': r'.*Fragment\.(java|kt)$',
            'Adapter': r'.*Adapter\.(java|kt)$',
            'Service': r'.*Service\.(java|kt)$',
            'Receiver': r'.*Receiver\.(java|kt)$',
            'Provider': r'.*Provider\.(java|kt)$',
            'ViewModel': r'.*ViewModel\.(java|kt)$',
            'Utils': r'.*Utils?\.(java|kt)$'
        }

    def analyze_project(self) -> Dict[str, List[str]]:
        """Analyze the entire project structure and identify files that need renaming"""
        print("🔍 Analyzing Android project structure...")
        
        analysis = {
            'layout_files': [],
            'java_files': [],
            'kotlin_files': [],
            'drawable_files': [],
            'menu_files': [],
            'issues': []
        }
        
        # Analyze layout files
        layout_dir = self.res_path / "layout"
        if layout_dir.exists():
            for layout_file in layout_dir.glob("*.xml"):
                analysis['layout_files'].append(str(layout_file))
        
        # Analyze Java/Kotlin files
        if self.java_path.exists():
            for java_file in self.java_path.rglob("*.java"):
                analysis['java_files'].append(str(java_file))
            for kt_file in self.java_path.rglob("*.kt"):
                analysis['kotlin_files'].append(str(kt_file))
        
        # Analyze drawable files
        drawable_dir = self.res_path / "drawable"
        if drawable_dir.exists():
            for drawable_file in drawable_dir.glob("*"):
                analysis['drawable_files'].append(str(drawable_file))
        
        # Analyze menu files
        menu_dir = self.res_path / "menu"
        if menu_dir.exists():
            for menu_file in menu_dir.glob("*.xml"):
                analysis['menu_files'].append(str(menu_file))
        
        print(f"📊 Found {len(analysis['layout_files'])} layout files")
        print(f"📊 Found {len(analysis['java_files'])} Java files")
        print(f"📊 Found {len(analysis['kotlin_files'])} Kotlin files")
        print(f"📊 Found {len(analysis['drawable_files'])} drawable files")
        print(f"📊 Found {len(analysis['menu_files'])} menu files")
        
        return analysis

    def identify_layout_renames(self, layout_files: List[str]) -> List[FileRename]:
        """Identify layout files that need renaming"""
        renames = []
        
        for layout_file in layout_files:
            file_path = Path(layout_file)
            filename = file_path.name
            
            # Skip files that already follow conventions
            if any(re.match(pattern, filename) for pattern in self.layout_patterns.values()):
                continue
            
            # Determine the type and new name
            new_name = self._determine_layout_name(file_path)
            if new_name and new_name != filename:
                renames.append(FileRename(
                    old_path=str(file_path),
                    new_path=str(file_path.parent / new_name),
                    old_name=filename,
                    new_name=new_name,
                    file_type="layout",
                    reason=f"Follow Android layout naming convention"
                ))
        
        return renames

    def _determine_layout_name(self, file_path: Path) -> str:
        """Determine the appropriate name for a layout file"""
        filename = file_path.name
        base_name = filename.replace('.xml', '')
        
        # Check if it's an activity layout by looking at the corresponding Java file
        if self._is_activity_layout(file_path):
            return f"activity_{base_name}.xml"
        
        # Check if it's a fragment layout
        if self._is_fragment_layout(file_path):
            return f"fragment_{base_name}.xml"
        
        # Check if it's a dialog
        if 'dialog' in base_name.lower():
            return f"dialog_{base_name}.xml"
        
        # Check if it's a bottom sheet
        if 'bottom' in base_name.lower() or 'sheet' in base_name.lower():
            return f"bottom_sheet_{base_name}.xml"
        
        # Check if it's an item layout
        if any(keyword in base_name.lower() for keyword in ['item', 'row', 'list_item']):
            return f"item_{base_name}.xml"
        
        # Check if it's an include layout
        if 'include' in base_name.lower() or 'merge' in base_name.lower():
            return f"include_{base_name}.xml"
        
        return filename  # No change needed

    def _is_activity_layout(self, file_path: Path) -> bool:
        """Check if a layout file belongs to an Activity"""
        filename = file_path.name.replace('.xml', '')
        
        # Look for corresponding Activity class
        for java_file in self.java_path.rglob("*.java"):
            if java_file.stem == filename and 'Activity' in java_file.stem:
                return True
        
        for kt_file in self.java_path.rglob("*.kt"):
            if kt_file.stem == filename and 'Activity' in kt_file.stem:
                return True
        
        return False

    def _is_fragment_layout(self, file_path: Path) -> bool:
        """Check if a layout file belongs to a Fragment"""
        filename = file_path.name.replace('.xml', '')
        
        # Look for corresponding Fragment class
        for java_file in self.java_path.rglob("*.java"):
            if java_file.stem == filename and 'Fragment' in java_file.stem:
                return True
        
        for kt_file in self.java_path.rglob("*.kt"):
            if kt_file.stem == filename and 'Fragment' in kt_file.stem:
                return True
        
        return False

    def identify_java_kotlin_renames(self, java_files: List[str], kotlin_files: List[str]) -> List[FileRename]:
        """Identify Java/Kotlin files that need renaming"""
        renames = []
        
        all_files = java_files + kotlin_files
        
        for file_path in all_files:
            file_obj = Path(file_path)
            filename = file_obj.name
            
            # Skip files that already follow conventions
            if any(re.match(pattern, filename) for pattern in self.java_patterns.values()):
                continue
            
            # Determine the type and new name
            new_name = self._determine_java_kotlin_name(file_obj)
            if new_name and new_name != filename:
                renames.append(FileRename(
                    old_path=str(file_obj),
                    new_path=str(file_obj.parent / new_name),
                    old_name=filename,
                    new_name=new_name,
                    file_type="java/kotlin",
                    reason=f"Follow Android class naming convention"
                ))
        
        return renames

    def _determine_java_kotlin_name(self, file_path: Path) -> str:
        """Determine the appropriate name for a Java/Kotlin file"""
        filename = file_path.name
        base_name = filename.replace('.java', '').replace('.kt', '')
        extension = '.java' if filename.endswith('.java') else '.kt'
        
        # Read the file to determine the class type
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()
                
                # Check for Activity
                if 'extends AppCompatActivity' in content or 'extends Activity' in content:
                    if not base_name.endswith('Activity'):
                        return f"{base_name}Activity{extension}"
                
                # Check for Fragment
                if 'extends Fragment' in content or 'extends DialogFragment' in content:
                    if not base_name.endswith('Fragment'):
                        return f"{base_name}Fragment{extension}"
                
                # Check for Adapter
                if 'extends RecyclerView.Adapter' in content or 'extends BaseAdapter' in content:
                    if not base_name.endswith('Adapter'):
                        return f"{base_name}Adapter{extension}"
                
                # Check for Service
                if 'extends Service' in content:
                    if not base_name.endswith('Service'):
                        return f"{base_name}Service{extension}"
                
                # Check for BroadcastReceiver
                if 'extends BroadcastReceiver' in content:
                    if not base_name.endswith('Receiver'):
                        return f"{base_name}Receiver{extension}"
                
                # Check for ContentProvider
                if 'extends ContentProvider' in content:
                    if not base_name.endswith('Provider'):
                        return f"{base_name}Provider{extension}"
                
                # Check for ViewModel
                if 'extends ViewModel' in content:
                    if not base_name.endswith('ViewModel'):
                        return f"{base_name}ViewModel{extension}"
                
                # Check for Utils
                if 'Utils' in base_name or 'Util' in base_name:
                    if not base_name.endswith('Utils') and not base_name.endswith('Util'):
                        return f"{base_name}Utils{extension}"
        
        except Exception as e:
            print(f"⚠️  Error reading {file_path}: {e}")
        
        return filename  # No change needed

    def identify_drawable_renames(self, drawable_files: List[str]) -> List[FileRename]:
        """Identify drawable files that need renaming"""
        renames = []
        
        for drawable_file in drawable_files:
            file_path = Path(drawable_file)
            filename = file_path.name
            
            # Skip files that already follow conventions
            if filename.startswith(('ic_', 'bg_', 'button_', 'selector_')):
                continue
            
            # Determine the type and new name
            new_name = self._determine_drawable_name(file_path)
            if new_name and new_name != filename:
                renames.append(FileRename(
                    old_path=str(file_path),
                    new_path=str(file_path.parent / new_name),
                    old_name=filename,
                    new_name=new_name,
                    file_type="drawable",
                    reason=f"Follow Android drawable naming convention"
                ))
        
        return renames

    def _determine_drawable_name(self, file_path: Path) -> str:
        """Determine the appropriate name for a drawable file"""
        filename = file_path.name
        base_name = filename.replace('.xml', '').replace('.png', '').replace('.jpg', '').replace('.jpeg', '')
        extension = file_path.suffix
        
        # Check if it's an icon
        if any(keyword in base_name.lower() for keyword in ['icon', 'ic_', 'arrow', 'close', 'add', 'delete', 'edit']):
            if not base_name.startswith('ic_'):
                return f"ic_{base_name}{extension}"
        
        # Check if it's a background
        if any(keyword in base_name.lower() for keyword in ['background', 'bg_', 'bg', 'shape', 'gradient']):
            if not base_name.startswith('bg_'):
                return f"bg_{base_name}{extension}"
        
        # Check if it's a button
        if 'button' in base_name.lower():
            return f"button_{base_name}{extension}"
        
        return filename  # No change needed

    def identify_menu_renames(self, menu_files: List[str]) -> List[FileRename]:
        """Identify menu files that need renaming"""
        renames = []
        
        for menu_file in menu_files:
            file_path = Path(menu_file)
            filename = file_path.name
            
            # Skip files that already follow conventions
            if filename.startswith('menu_'):
                continue
            
            # Add menu_ prefix
            new_name = f"menu_{filename}"
            renames.append(FileRename(
                old_path=str(file_path),
                new_path=str(file_path.parent / new_name),
                old_name=filename,
                new_name=new_name,
                file_type="menu",
                reason=f"Follow Android menu naming convention"
            ))
        
        return renames

    def find_references_to_rename(self, file_rename: FileRename) -> List[ReferenceUpdate]:
        """Find all references to a file that need to be updated"""
        references = []
        old_name = file_rename.old_name.replace('.xml', '').replace('.java', '').replace('.kt', '')
        new_name = file_rename.new_name.replace('.xml', '').replace('.java', '').replace('.kt', '')
        
        # Search in Java/Kotlin files
        for java_file in self.java_path.rglob("*.java"):
            references.extend(self._find_references_in_file(java_file, old_name, new_name, file_rename.file_type))
        
        for kt_file in self.java_path.rglob("*.kt"):
            references.extend(self._find_references_in_file(kt_file, old_name, new_name, file_rename.file_type))
        
        # Search in XML files
        for xml_file in self.res_path.rglob("*.xml"):
            references.extend(self._find_references_in_file(xml_file, old_name, new_name, file_rename.file_type))
        
        return references

    def _find_references_in_file(self, file_path: Path, old_name: str, new_name: str, file_type: str) -> List[ReferenceUpdate]:
        """Find references to a file within a specific file"""
        references = []
        
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                lines = f.readlines()
                
                for i, line in enumerate(lines):
                    line_num = i + 1
                    
                    # Look for R.layout references
                    if file_type == "layout" and f"R.layout.{old_name}" in line:
                        new_line = line.replace(f"R.layout.{old_name}", f"R.layout.{new_name}")
                        references.append(ReferenceUpdate(
                            file_path=str(file_path),
                            old_reference=f"R.layout.{old_name}",
                            new_reference=f"R.layout.{new_name}",
                            line_number=line_num,
                            context=line.strip()
                        ))
                    
                    # Look for class references
                    elif file_type == "java/kotlin" and old_name in line:
                        # More sophisticated pattern matching for class references
                        patterns = [
                            rf'\b{re.escape(old_name)}\b',  # Word boundary
                            rf'class\s+{re.escape(old_name)}\b',
                            rf'new\s+{re.escape(old_name)}\(',
                            rf'{re.escape(old_name)}\s*\(',
                        ]
                        
                        for pattern in patterns:
                            if re.search(pattern, line):
                                new_line = re.sub(pattern, new_name, line)
                                references.append(ReferenceUpdate(
                                    file_path=str(file_path),
                                    old_reference=old_name,
                                    new_reference=new_name,
                                    line_number=line_num,
                                    context=line.strip()
                                ))
                                break
                    
                    # Look for tools:context references
                    elif file_type == "layout" and f'tools:context=".{old_name}"' in line:
                        new_line = line.replace(f'tools:context=".{old_name}"', f'tools:context=".{new_name}"')
                        references.append(ReferenceUpdate(
                            file_path=str(file_path),
                            old_reference=f'tools:context=".{old_name}"',
                            new_reference=f'tools:context=".{new_name}"',
                            line_number=line_num,
                            context=line.strip()
                        ))
        
        except Exception as e:
            print(f"⚠️  Error reading {file_path}: {e}")
        
        return references

    def execute_renames(self, renames: List[FileRename], dry_run: bool = True) -> None:
        """Execute the file renames"""
        if dry_run:
            print("\n🔍 DRY RUN - No files will be modified")
        else:
            print("\n🚀 Executing file renames...")
        
        for rename in renames:
            if dry_run:
                print(f"📝 Would rename: {rename.old_path} → {rename.new_path}")
                print(f"   Reason: {rename.reason}")
            else:
                try:
                    # Create backup
                    backup_path = f"{rename.old_path}.backup"
                    shutil.copy2(rename.old_path, backup_path)
                    
                    # Rename the file
                    os.rename(rename.old_path, rename.new_path)
                    
                    # Update name mapping for reference updates
                    self.name_mapping[rename.old_name] = rename.new_name
                    
                    print(f"✅ Renamed: {rename.old_path} → {rename.new_path}")
                    
                except Exception as e:
                    print(f"❌ Error renaming {rename.old_path}: {e}")
        
        self.file_renames.extend(renames)

    def execute_reference_updates(self, references: List[ReferenceUpdate], dry_run: bool = True) -> None:
        """Execute the reference updates"""
        if dry_run:
            print("\n🔍 DRY RUN - No references will be updated")
        else:
            print("\n🔄 Updating references...")
        
        # Group references by file
        file_references = defaultdict(list)
        for ref in references:
            file_references[ref.file_path].append(ref)
        
        for file_path, refs in file_references.items():
            if dry_run:
                print(f"📝 Would update {len(refs)} references in: {file_path}")
                for ref in refs:
                    print(f"   Line {ref.line_number}: {ref.old_reference} → {ref.new_reference}")
            else:
                try:
                    # Read the file
                    with open(file_path, 'r', encoding='utf-8') as f:
                        lines = f.readlines()
                    
                    # Apply updates
                    for ref in refs:
                        if ref.line_number <= len(lines):
                            lines[ref.line_number - 1] = lines[ref.line_number - 1].replace(
                                ref.old_reference, ref.new_reference
                            )
                    
                    # Write back
                    with open(file_path, 'w', encoding='utf-8') as f:
                        f.writelines(lines)
                    
                    print(f"✅ Updated {len(refs)} references in: {file_path}")
                    
                except Exception as e:
                    print(f"❌ Error updating references in {file_path}: {e}")
        
        self.reference_updates.extend(references)

    def generate_report(self) -> str:
        """Generate a detailed summary report"""
        report = []
        report.append("=" * 80)
        report.append("ANDROID PROJECT REFACTORING REPORT")
        report.append("=" * 80)
        report.append("")
        
        # File renames summary
        report.append("📁 FILE RENAMES:")
        report.append("-" * 40)
        if self.file_renames:
            for rename in self.file_renames:
                report.append(f"• {rename.old_name} → {rename.new_name}")
                report.append(f"  Type: {rename.file_type}")
                report.append(f"  Reason: {rename.reason}")
                report.append("")
        else:
            report.append("No files were renamed.")
            report.append("")
        
        # Reference updates summary
        report.append("🔗 REFERENCE UPDATES:")
        report.append("-" * 40)
        if self.reference_updates:
            # Group by file
            file_refs = defaultdict(list)
            for ref in self.reference_updates:
                file_refs[ref.file_path].append(ref)
            
            for file_path, refs in file_refs.items():
                report.append(f"📄 {file_path}:")
                for ref in refs:
                    report.append(f"  Line {ref.line_number}: {ref.old_reference} → {ref.new_reference}")
                report.append("")
        else:
            report.append("No references were updated.")
            report.append("")
        
        # Statistics
        report.append("📊 STATISTICS:")
        report.append("-" * 40)
        report.append(f"Total files renamed: {len(self.file_renames)}")
        report.append(f"Total references updated: {len(self.reference_updates)}")
        
        # Breakdown by type
        type_counts = defaultdict(int)
        for rename in self.file_renames:
            type_counts[rename.file_type] += 1
        
        if type_counts:
            report.append("\nBreakdown by file type:")
            for file_type, count in type_counts.items():
                report.append(f"  {file_type}: {count} files")
        
        report.append("")
        report.append("=" * 80)
        
        return "\n".join(report)

    def run_refactoring(self, dry_run: bool = True) -> None:
        """Run the complete refactoring process"""
        print("🚀 Starting Android Project Refactoring Agent")
        print("=" * 60)
        
        # Step 1: Analyze project
        analysis = self.analyze_project()
        
        # Step 2: Identify renames
        print("\n🔍 Identifying files that need renaming...")
        
        layout_renames = self.identify_layout_renames(analysis['layout_files'])
        java_renames = self.identify_java_kotlin_renames(analysis['java_files'], analysis['kotlin_files'])
        drawable_renames = self.identify_drawable_renames(analysis['drawable_files'])
        menu_renames = self.identify_menu_renames(analysis['menu_files'])
        
        all_renames = layout_renames + java_renames + drawable_renames + menu_renames
        
        print(f"📋 Found {len(all_renames)} files that need renaming:")
        print(f"  • Layout files: {len(layout_renames)}")
        print(f"  • Java/Kotlin files: {len(java_renames)}")
        print(f"  • Drawable files: {len(drawable_renames)}")
        print(f"  • Menu files: {len(menu_renames)}")
        
        # Step 3: Find references
        print("\n🔍 Finding references to update...")
        all_references = []
        for rename in all_renames:
            references = self.find_references_to_rename(rename)
            all_references.extend(references)
        
        print(f"📋 Found {len(all_references)} references to update")
        
        # Step 4: Execute renames
        self.execute_renames(all_renames, dry_run)
        
        # Step 5: Execute reference updates
        self.execute_reference_updates(all_references, dry_run)
        
        # Step 6: Generate report
        report = self.generate_report()
        print("\n" + report)
        
        # Save report to file
        report_file = self.project_path / "refactoring_report.txt"
        with open(report_file, 'w', encoding='utf-8') as f:
            f.write(report)
        
        print(f"\n📄 Detailed report saved to: {report_file}")

def main():
    """Main entry point"""
    if len(sys.argv) < 2:
        print("Usage: python3 android_refactor_agent.py <project_path> [--execute]")
        print("  --execute: Actually perform the renames (default is dry run)")
        sys.exit(1)
    
    project_path = sys.argv[1]
    dry_run = "--execute" not in sys.argv
    
    if not os.path.exists(project_path):
        print(f"❌ Project path does not exist: {project_path}")
        sys.exit(1)
    
    agent = AndroidRefactorAgent(project_path)
    agent.run_refactoring(dry_run=dry_run)

if __name__ == "__main__":
    main()