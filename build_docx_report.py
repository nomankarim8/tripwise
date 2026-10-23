import os
import re
from docx import Document
from docx.shared import Inches, Pt, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT, WD_ALIGN_VERTICAL
from docx.oxml import parse_xml, OxmlElement
from docx.oxml.ns import nsdecls, qn

def set_cell_background(cell, fill_hex):
    tcPr = cell._tc.get_or_add_tcPr()
    shd = parse_xml(f'<w:shd {nsdecls("w")} w:fill="{fill_hex}"/>')
    tcPr.append(shd)

def set_cell_margins(cell, top=120, bottom=120, left=160, right=160):
    tcPr = cell._tc.get_or_add_tcPr()
    tcMar = parse_xml(f'<w:tcMar {nsdecls("w")}><w:top w:w="{top}" w:type="dxa"/><w:bottom w:w="{bottom}" w:type="dxa"/><w:left w:w="{left}" w:type="dxa"/><w:right w:w="{right}" w:type="dxa"/></w:tcMar>')
    tcPr.append(tcMar)

def set_table_borders(table, color="CBD5E1"):
    tblPr = table._tbl.tblPr
    borders = parse_xml(
        f'<w:tblBorders {nsdecls("w")}>'
        f'  <w:top w:val="single" w:sz="6" w:space="0" w:color="{color}"/>'
        f'  <w:bottom w:val="single" w:sz="6" w:space="0" w:color="{color}"/>'
        f'  <w:left w:val="none"/>'
        f'  <w:right w:val="none"/>'
        f'  <w:insideH w:val="single" w:sz="4" w:space="0" w:color="{color}"/>'
        f'  <w:insideV w:val="none"/>'
        f'</w:tblBorders>'
    )
    tblPr.append(borders)

def build_docx():
    print("Combining all chapter files from report/ folder...")
    chapter_files = [
        'report/00_preliminary.md',
        'report/01_introduction.md',
        'report/02_literature_review.md',
        'report/03_srs.md',
        'report/04_system_design.md',
        'report/05_implementation.md',
        'report/06_testing.md',
        'report/07_limitations_future_work.md',
        'report/08_conclusion_references.md',
        'report/09_appendices.md'
    ]

    full_content = []
    for fpath in chapter_files:
        if os.path.exists(fpath):
            with open(fpath, 'r', encoding='utf-8') as f:
                full_content.append(f.read())
        else:
            print(f"Warning: {fpath} not found!")

    master_md = '\n\n<div style="page-break-after: always;"></div>\n\n'.join(full_content)
    with open("TripWise_Software_Engineering_Project_Report.md", "w", encoding="utf-8") as f:
        f.write(master_md)
    print("TripWise_Software_Engineering_Project_Report.md updated with all chapters and figure links.")

    print("Initializing styled Document...")
    doc = Document()

    # Configure Margins: 1 inch (72 pt) all around
    for section in doc.sections:
        section.top_margin = Inches(1.0)
        section.bottom_margin = Inches(1.0)
        section.left_margin = Inches(1.0)
        section.right_margin = Inches(1.0)
        section.different_first_page_header_footer = True
        
        # Header / Footer
        header = section.header
        hp = header.paragraphs[0]
        hp.alignment = WD_ALIGN_PARAGRAPH.RIGHT
        hrun = hp.add_run("TripWise: Smart Vehicle & Trip Cost Planner — Capstone Engineering Report")
        hrun.font.name = "Calibri"
        hrun.font.size = Pt(8.5)
        hrun.font.color.rgb = RGBColor(100, 116, 139)

        footer = section.footer
        fp = footer.paragraphs[0]
        fp.alignment = WD_ALIGN_PARAGRAPH.CENTER
        frun = fp.add_run("Department of Computer Science & Engineering | Software Engineering Project")
        frun.font.name = "Calibri"
        frun.font.size = Pt(8.5)
        frun.font.color.rgb = RGBColor(100, 116, 139)

    # Styles
    normal_style = doc.styles['Normal']
    normal_style.font.name = 'Calibri'
    normal_style.font.size = Pt(10.5)
    normal_style.font.color.rgb = RGBColor(30, 41, 59)
    normal_style.paragraph_format.line_spacing = 1.15
    normal_style.paragraph_format.space_after = Pt(5)

    lines = master_md.splitlines()

    in_code_block = False
    code_block_lines = []
    in_table = False
    table_lines = []
    first_h1 = True

    def flush_table():
        nonlocal in_table, table_lines
        if not table_lines:
            in_table = False
            return
        
        rows_data = []
        for line in table_lines:
            s = line.strip()
            if not s or s.startswith("| :---") or s.startswith("|:---") or s.startswith("|---"):
                continue
            cells = [c.strip() for c in s.strip("|").split("|")]
            if cells:
                rows_data.append(cells)
        
        if rows_data:
            num_cols = max(len(r) for r in rows_data)
            for r in rows_data:
                while len(r) < num_cols:
                    r.append("")
            
            table = doc.add_table(rows=len(rows_data), cols=num_cols)
            table.alignment = WD_TABLE_ALIGNMENT.CENTER
            set_table_borders(table)

            for row_idx, row in enumerate(rows_data):
                is_header = (row_idx == 0)
                for col_idx, cell_text in enumerate(row):
                    cell = table.cell(row_idx, col_idx)
                    cell.vertical_alignment = WD_ALIGN_VERTICAL.CENTER
                    set_cell_margins(cell, top=100, bottom=100, left=140, right=140)
                    
                    if is_header:
                        set_cell_background(cell, "006874") # Deep Teal
                    elif row_idx % 2 == 1:
                        set_cell_background(cell, "F8FAFC") # Crisp Slate Tint
                    else:
                        set_cell_background(cell, "FFFFFF")
                    
                    p = cell.paragraphs[0]
                    p.paragraph_format.space_after = Pt(2)
                    p.paragraph_format.space_before = Pt(2)
                    p.paragraph_format.line_spacing = 1.05
                    
                    clean_text = cell_text.replace("<br>", "\n").replace("**", "")
                    run = p.add_run(clean_text)
                    run.font.name = "Calibri"
                    run.font.size = Pt(9.0)
                    if is_header:
                        run.font.bold = True
                        run.font.color.rgb = RGBColor(255, 255, 255)
                    else:
                        run.font.color.rgb = RGBColor(30, 41, 59)
            
            sp = doc.add_paragraph()
            sp.paragraph_format.space_after = Pt(4)
            sp.paragraph_format.space_before = Pt(0)
            
        table_lines = []
        in_table = False

    def flush_code_block():
        nonlocal in_code_block, code_block_lines
        if not code_block_lines:
            in_code_block = False
            return
        
        full_code = "\n".join(code_block_lines)
        
        # Callout box for code / ASCII
        tbl = doc.add_table(rows=1, cols=1)
        tbl.alignment = WD_TABLE_ALIGNMENT.CENTER
        cell = tbl.cell(0, 0)
        set_cell_background(cell, "F1F5F9")
        set_cell_margins(cell, top=100, bottom=100, left=180, right=180)
        
        tcPr = cell._tc.get_or_add_tcPr()
        borders = parse_xml(
            f'<w:tcBorders {nsdecls("w")}>'
            f'  <w:left w:val="single" w:sz="24" w:space="0" w:color="006874"/>'
            f'  <w:top w:val="single" w:sz="4" w:space="0" w:color="E2E8F0"/>'
            f'  <w:right w:val="single" w:sz="4" w:space="0" w:color="E2E8F0"/>'
            f'  <w:bottom w:val="single" w:sz="4" w:space="0" w:color="E2E8F0"/>'
            f'</w:tcBorders>'
        )
        tcPr.append(borders)

        cp = cell.paragraphs[0]
        cp.paragraph_format.space_before = Pt(3)
        cp.paragraph_format.space_after = Pt(3)
        cp.paragraph_format.line_spacing = 1.05
        
        crun = cp.add_run(full_code)
        crun.font.name = "Consolas"
        crun.font.size = Pt(8.2)
        crun.font.color.rgb = RGBColor(15, 23, 42)

        sp = doc.add_paragraph()
        sp.paragraph_format.space_after = Pt(4)

        code_block_lines = []
        in_code_block = False

    print("Parsing lines and embedding text, tables, and visual diagram figures...")

    for line in lines:
        stripped = line.strip()

        # Handle Page Breaks
        if '<div style="page-break-after: always;"></div>' in stripped or stripped == "---":
            if in_table:
                flush_table()
            if in_code_block:
                flush_code_block()
            if stripped != "---":
                doc.add_page_break()
            continue

        # Handle Markdown Images: ![caption](image_path)
        img_match = re.match(r'^!\[(.*?)\]\((.*?)\)$', stripped)
        if img_match:
            if in_table:
                flush_table()
            if in_code_block:
                flush_code_block()
            
            caption, img_rel_path = img_match.groups()
            img_full_path = img_rel_path.strip()
            if not os.path.isabs(img_full_path):
                img_full_path = os.path.join(os.getcwd(), img_full_path)
            
            if os.path.exists(img_full_path):
                print(f"Embedding visual diagram: {img_full_path}")
                img_p = doc.add_paragraph()
                img_p.alignment = WD_ALIGN_PARAGRAPH.CENTER
                img_p.paragraph_format.space_before = Pt(8)
                img_p.paragraph_format.space_after = Pt(4)
                
                # Add picture scaled to 6.2 inches (fits 1-inch margins on 8.5x11 page)
                run = img_p.add_run()
                run.add_picture(img_full_path, width=Inches(6.2))
            else:
                print(f"Warning: Image file not found: {img_full_path}")
            continue

        # Handle Figure Captions (*Figure ...*)
        if stripped.startswith("*Figure") and stripped.endswith("*"):
            if in_table:
                flush_table()
            if in_code_block:
                flush_code_block()
            
            cap_p = doc.add_paragraph()
            cap_p.alignment = WD_ALIGN_PARAGRAPH.CENTER
            cap_p.paragraph_format.space_before = Pt(2)
            cap_p.paragraph_format.space_after = Pt(10)
            
            run = cap_p.add_run(stripped.strip("*"))
            run.font.name = "Calibri"
            run.font.size = Pt(9.5)
            run.font.italic = True
            run.font.bold = True
            run.font.color.rgb = RGBColor(71, 85, 105)
            continue

        # Handle Code blocks
        if stripped.startswith("```"):
            if in_table:
                flush_table()
            if in_code_block:
                flush_code_block()
            else:
                in_code_block = True
                code_block_lines = []
            continue

        if in_code_block:
            code_block_lines.append(line)
            continue

        # Handle Tables
        if stripped.startswith("|") and stripped.endswith("|"):
            in_table = True
            table_lines.append(stripped)
            continue
        elif in_table:
            flush_table()

        # Handle Heading 1 (Chapters / Sections)
        if stripped.startswith("# "):
            heading_text = stripped[2:].strip()
            if not first_h1:
                doc.add_page_break()
            first_h1 = False
            
            p = doc.add_paragraph()
            p.paragraph_format.space_before = Pt(18)
            p.paragraph_format.space_after = Pt(8)
            p.paragraph_format.keep_with_next = True
            run = p.add_run(heading_text)
            run.font.name = "Calibri"
            run.font.size = Pt(19)
            run.font.bold = True
            run.font.color.rgb = RGBColor(0, 104, 116) # Deep Teal
            continue

        # Handle Heading 2
        if stripped.startswith("## "):
            heading_text = stripped[3:].strip()
            p = doc.add_paragraph()
            p.paragraph_format.space_before = Pt(14)
            p.paragraph_format.space_after = Pt(5)
            p.paragraph_format.keep_with_next = True
            run = p.add_run(heading_text)
            run.font.name = "Calibri"
            run.font.size = Pt(13.5)
            run.font.bold = True
            run.font.color.rgb = RGBColor(27, 78, 91)
            continue

        # Handle Heading 3
        if stripped.startswith("### "):
            heading_text = stripped[4:].strip()
            p = doc.add_paragraph()
            p.paragraph_format.space_before = Pt(10)
            p.paragraph_format.space_after = Pt(3)
            p.paragraph_format.keep_with_next = True
            run = p.add_run(heading_text)
            run.font.name = "Calibri"
            run.font.size = Pt(11.5)
            run.font.bold = True
            run.font.color.rgb = RGBColor(30, 41, 59)
            continue

        # Handle Heading 4
        if stripped.startswith("#### "):
            heading_text = stripped[5:].strip()
            p = doc.add_paragraph()
            p.paragraph_format.space_before = Pt(8)
            p.paragraph_format.space_after = Pt(2)
            p.paragraph_format.keep_with_next = True
            run = p.add_run(heading_text)
            run.font.name = "Calibri"
            run.font.size = Pt(10.5)
            run.font.bold = True
            run.font.color.rgb = RGBColor(71, 85, 105)
            continue

        if not stripped:
            continue

        # Handle Bullet Points
        if stripped.startswith("- ") or stripped.startswith("* "):
            bullet_text = stripped[2:].strip()
            p = doc.add_paragraph(style='List Bullet')
            p.paragraph_format.space_after = Pt(2.5)
            p.paragraph_format.line_spacing = 1.15
            
            parts = re.split(r'(\*\*.*?\*\*)', bullet_text)
            for part in parts:
                if part.startswith("**") and part.endswith("**"):
                    r = p.add_run(part[2:-2])
                    r.font.bold = True
                    r.font.color.rgb = RGBColor(15, 23, 42)
                else:
                    p.add_run(part)
            continue

        # Handle Numbered Lists
        num_match = re.match(r'^(\d+)\.\s+(.*)', stripped)
        if num_match:
            num_str, item_text = num_match.groups()
            p = doc.add_paragraph(style='List Number')
            p.paragraph_format.space_after = Pt(2.5)
            p.paragraph_format.line_spacing = 1.15
            
            parts = re.split(r'(\*\*.*?\*\*)', item_text)
            for part in parts:
                if part.startswith("**") and part.endswith("**"):
                    r = p.add_run(part[2:-2])
                    r.font.bold = True
                    r.font.color.rgb = RGBColor(15, 23, 42)
                else:
                    p.add_run(part)
            continue

        # Regular Body Paragraph
        p = doc.add_paragraph()
        p.paragraph_format.space_after = Pt(5)
        p.paragraph_format.line_spacing = 1.15
        
        parts = re.split(r'(\*\*.*?\*\*)', stripped)
        for part in parts:
            if part.startswith("**") and part.endswith("**"):
                r = p.add_run(part[2:-2])
                r.font.bold = True
                r.font.color.rgb = RGBColor(15, 23, 42)
            else:
                p.add_run(part)

    if in_table:
        flush_table()
    if in_code_block:
        flush_code_block()

    docx_path = "TripWise_Software_Engineering_Project_Report.docx"
    doc.save(docx_path)
    file_size_kb = os.path.getsize(docx_path) / 1024
    print(f"Successfully compiled DESIGNED DOCX report with embedded diagrams at: {docx_path}")
    print(f"Total file size: {file_size_kb:.2f} KB")

if __name__ == "__main__":
    build_docx()
