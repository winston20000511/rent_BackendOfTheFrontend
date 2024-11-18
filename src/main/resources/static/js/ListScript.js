
export const param ={}

export function init(){
    param.list = document.getElementById('link-list-1')
    param.header = document.getElementById('header')

    if (!param.list || !param.header) {
        console.error('部分元素未找到，請檢查 DOM 結構或選擇器');
    }
}

export function linkMouseenter(){
    const {list , header} = param;
    const rect = window.getComputedStyle(header);
    list.style.top = rect.height;
    list.style.display='block';
}

export function linkMouseleave(){
    const {list} = param;
    setTimeout(()=>{
        if(!list.matches(':hover')){
            list.style.display= 'none';
        }
    },400)
}

export function listMouseenter(event){
    const list = event.currentTarget;
    list.style.display='block';
}

export function listMouseleave(){
    const list = event.currentTarget;
    list.style.display='none';
}

