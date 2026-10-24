const fs = require('fs');
const {
  Document, Packer, Paragraph, TextRun, HeadingLevel, AlignmentType,
  PageBreak, ImageRun, Table, TableRow, TableCell, WidthType, ShadingType,
  BorderStyle, VerticalAlign
} = require('docx');

const out = 'd:/githubProject/SHOPONLINE/onlineMall';
const navy = '16324F';
const teal = '0B7285';
const light = 'EAF4F4';
const gray = '4B5563';
const border = { style: BorderStyle.SINGLE, size: 1, color: 'D5DDE5' };
const borders = { top: border, bottom: border, left: border, right: border };
const font = { ascii: 'Arial', hAnsi: 'Arial', eastAsia: 'Microsoft YaHei' };
const normal = { font, size: 22, color: '243447' };

function p(text, opts = {}) {
  return new Paragraph({ spacing: { after: opts.after ?? 120, line: 276 }, alignment: opts.alignment, children: [new TextRun({ text, ...normal, bold: opts.bold, color: opts.color, size: opts.size })] });
}
function rich(runs, opts = {}) {
  return new Paragraph({ spacing: { after: opts.after ?? 120, line: 276 }, alignment: opts.alignment, children: runs.map(x => new TextRun({ ...normal, ...x })) });
}
function bullet(text) { return new Paragraph({ numbering: { reference: 'bullets', level: 0 }, spacing: { after: 80, line: 250 }, children: [new TextRun({ text, ...normal })] }); }
function cell(text, fill) { return new TableCell({ borders, width: { size: 4680, type: WidthType.DXA }, shading: fill ? { fill, type: ShadingType.CLEAR } : undefined, margins: { top: 100, bottom: 100, left: 140, right: 140 }, verticalAlign: VerticalAlign.CENTER, children: [p(text, { after: 0, size: 20 })] }); }
function title(text, subtitle) { return [p(text, { bold: true, color: navy, size: 34, after: 60 }), p(subtitle, { color: teal, size: 21, after: 300 })]; }

const cover = [
  ...title('AI / Machine Learning Solutions for Geospatial Decision Intelligence', 'A concise technical approach prepared for the Upwork invitation'),
  p('Prepared by Mansen Nan | AI Applications Engineer | LLM Agents | Intelligent Automation | Python / Java', { color: gray, after: 220 }),
  p('Objective', { bold: true, color: navy, size: 25, after: 80 }),
  p('Design and deliver a reliable machine-learning system that combines geospatial data, domain features, model evaluation, and human review to improve operational efficiency and decision quality.'),
  p('Relevant background', { bold: true, color: navy, size: 25, after: 80 }),
  bullet('15+ years delivering production software across enterprise platforms, intelligent automation, computer vision, financial services, energy, manufacturing, and smart buildings.'),
  bullet('Python-based computer vision delivery: improved industrial defect-detection mAP@0.5 from 76.5% to 92.3%, optimized inference to 45 FPS on RTX 3060, and reduced manual inspection workload by approximately 50%.'),
  bullet('Production AI application experience: LLM agents, tool-using workflows, structured outputs, evaluation, API integration, observability, and human-in-the-loop controls.'),
  bullet('Strong systems foundation: distributed architecture, event-driven workflows, APIs, queues, Redis, Docker, testing, deployment, and stakeholder communication.'),
  p('Proposed delivery principle', { bold: true, color: navy, size: 25, after: 80 }),
  p('Start with a measurable decision workflow, establish a reproducible geospatial data pipeline, build a baseline model, and iterate through offline evaluation, pilot validation, monitoring, and operational handover. GIS-specific libraries and spatial infrastructure would be selected to match the project’s data sources and deployment environment.'),
  new Paragraph({ children: [new PageBreak()] }),
  p('Reference architecture', { bold: true, color: navy, size: 27, after: 120 }),
  p('Spatial data sources → geospatial data pipeline → ML / AI layer → decision services. Evaluation, monitoring, and stakeholder feedback form a continuous improvement loop.', { color: gray, size: 22, after: 160 }),
  new Table({ width: { size: 100, type: WidthType.PERCENTAGE }, columnWidths: [2340, 2340, 2340, 2340], rows: [
    new TableRow({ cantSplit: true, children: [cell('Data Sources\nGIS layers, imagery, IoT, historical outcomes', light), cell('Spatial Data Pipeline\nIngest, CRS, validation, feature engineering', light), cell('ML / AI Layer\nBaseline, spatial validation, confidence', light), cell('Decision Services\nPython API, map/dashboard, alerts, review', light)] }),
    new TableRow({ cantSplit: true, children: [cell('Evaluation & Governance\nMetrics, error analysis, audit, access controls'), cell('Operations & Monitoring\nLatency, drift, logs, retraining, rollback'), cell('Stakeholder Feedback\nReview, corrected outcomes, threshold tuning'), cell('Continuous Improvement\nValidated feedback returns to data and models')] })
  ]}),
  p('The diagram is intentionally implementation-neutral: it shows how spatial data, feature engineering, models, APIs, monitoring, and human review fit together. The exact stack can be adapted to the client’s existing cloud, database, GIS, and compliance requirements.', { color: gray, size: 19 })
];

const doc = new Document({
  styles: { default: { document: { run: normal } }, paragraphStyles: [
    { id: 'Heading1', name: 'Heading 1', basedOn: 'Normal', next: 'Normal', run: { size: 30, bold: true, color: navy, font }, paragraph: { spacing: { before: 240, after: 140 }, outlineLevel: 0, keepNext: false, keepLines: false } },
    { id: 'Heading2', name: 'Heading 2', basedOn: 'Normal', next: 'Normal', run: { size: 25, bold: true, color: teal, font }, paragraph: { spacing: { before: 180, after: 100 }, outlineLevel: 1, keepNext: false, keepLines: false } }
  ]},
  numbering: { config: [{ reference: 'bullets', levels: [{ level: 0, format: 'bullet', text: '•', alignment: AlignmentType.LEFT, style: { paragraph: { indent: { left: 520, hanging: 260 } } } }] }] },
  sections: [{ properties: { page: { size: { width: 12240, height: 15840 }, margin: { top: 1080, right: 1260, bottom: 1080, left: 1260 } } }, children: cover }]
});
Packer.toBuffer(doc).then(b => fs.writeFileSync(`${out}/Mansen_Nan_AI_GIS_Solution_Approach.docx`, b));

const letter = new Document({
  styles: { default: { document: { run: normal } }, paragraphStyles: [
    { id: 'Heading1', name: 'Heading 1', basedOn: 'Normal', next: 'Normal', run: { size: 31, bold: true, color: navy, font }, paragraph: { spacing: { before: 180, after: 120 }, outlineLevel: 0, keepNext: false, keepLines: false } }
  ]},
  sections: [{ properties: { page: { size: { width: 12240, height: 15840 }, margin: { top: 1080, right: 1260, bottom: 1080, left: 1260 } } }, children: [
    ...title('Cover Letter', 'AI / Machine Learning Expert — Python, GIS, and Intelligent Solutions'),
    rich([{ text: 'Dear Hiring Manager,', bold: true }], { after: 220 }),
    p('Thank you for inviting me to apply for your AI and machine learning project. Your need for someone who can develop algorithms, work with Python and GIS, and turn complex problems into effective machine-learning systems is closely aligned with my background as an AI Applications Engineer and senior software engineer.'),
    p('I bring 15+ years of production engineering experience across intelligent automation, computer vision, enterprise platforms, energy, manufacturing, financial services, and smart buildings. My focus is not only on building models, but on converting them into reliable systems that teams can use, measure, and operate.'),
    p('A particularly relevant example is my industrial computer-vision project for seven classes of surface defects. I improved mAP@0.5 from 76.5% to 92.3% through transfer learning, Mosaic and MixUp augmentation, multi-scale training, and hyperparameter tuning. I also optimized inference to 45 FPS on an RTX 3060, increased small-object recall from 63.1% to 78.3%, and reduced the model size from 14.4 MB to 6.7 MB. The resulting Python, PyTorch, YOLO, OpenCV, and Flask application was deployed to a quality-control line, achieving less than 3% false positives and reducing manual inspection workload by approximately 50%.'),
    p('In addition, I have designed and delivered LLM-powered agents and intelligent workflow applications involving tool integration, structured outputs, event-driven execution, queues, state machines, retries, observability, and human-in-the-loop controls. This combination of machine-learning implementation and production systems engineering allows me to connect data science work to measurable business outcomes.'),
    p('For a GIS-oriented engagement, I would begin by clarifying the decision to improve, the spatial and temporal data sources, the operational constraints, and the success metrics. I would then establish a reproducible geospatial data and feature pipeline, build a transparent baseline, evaluate candidate models with spatially appropriate validation, and expose results through an API or operational interface. Where decisions are sensitive or difficult to automate safely, I would include clear confidence thresholds, explanations, audit trails, and human review.'),
    p('I would be glad to discuss your use case, current data environment, and the first practical milestone. I can contribute across discovery, algorithm development, Python implementation, system architecture, testing, deployment, and technical handover.'),
    p('Thank you for your consideration. I look forward to learning more about the project and exploring how I can help your team build an effective AI-driven solution.'),
    rich([{ text: 'Best regards,', bold: true }], { after: 40 }),
    p('Mansen Nan', { bold: true, color: navy, after: 40 }),
    p('AI Applications Engineer | LLM Agents | Intelligent Automation | Python / Java', { color: gray, size: 20, after: 40 }),
    p('Remote — Europe / United States', { color: gray, size: 20, after: 0 })
  ]}]
});
Packer.toBuffer(letter).then(b => fs.writeFileSync(`${out}/Mansen_Nan_Upwork_Cover_Letter.docx`, b));
