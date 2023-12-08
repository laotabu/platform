// 对数组进行分组
export function groupBy(array, keyFunc, ValueFunc=null){
  let map = new Map();
  array.forEach((e)=>{
    // 根据传入的函数，对数组中的每一个对象产生一个key值
    let key = keyFunc(e);
    map.set(key, (map.get(key) || []));
    // 根据传入的函数，对值进行处理[如果有]
    let value = ValueFunc != null ? ValueFunc(e) : e;
    map.get(key).push(value);
  });
  return map;
}
