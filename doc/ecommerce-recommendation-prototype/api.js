function delay(ms=420){return new Promise(r=>setTimeout(r,ms));}
// TODO replace: GET /api/v1/recommend/scenarios; response {code,data,total}
async function fetchScenarios(){await delay();return {code:0,data:DB.scenarios,total:DB.scenarios.length};}
// TODO replace: GET /api/v1/experiments; response {code,data,total}
async function fetchExperiments(){await delay();return {code:0,data:DB.experiments,total:DB.experiments.length};}
// TODO replace: POST /api/v1/experiments/{id}/pause; response {code,data}
async function pauseExperiment(id){await delay(260);const item=DB.experiments.find(x=>x.id===id);if(item)item.status='ended';return {code:0,data:item};}
