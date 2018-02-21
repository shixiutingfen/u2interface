#ifndef QST_CompareFeatures_h
#define QST_CompareFeatures_h


#ifdef QST_SDK_EXPORTS
#define _SDK_API __declspec(dllexport)
#else   // QST_SDK_EXPORTS
#ifdef QST_SDK_IS_DLL
#define QST_SDK_API __declspec(dllimport)
#else   // QST_SDK_IS_DLL
#define QST_SDK_API
#endif  // QST_SDK_IS_DLL
#endif  // QST_SDK_EXPORTS

/**************************************************************************************************
*                                          QST 错误码
**************************************************************************************************/
typedef enum
{
    QST_SDK_ERR_NONE = 0,
    QST_SDK_ERR_INVALID_PARAM = -1

} QSTError;

#ifdef __cplusplus
extern "C" {
/**
*
*/
QST_SDK_API char* QST_GetCompareFeaturesVersion();

/**
* 比较特征数据
* @param   targetFeatures        -- [输入] 特征数据
* @param   targetFeaturesLength  -- [输入] 图片特征数据长度
* @param   queryFeatures         -- [输入] 特征数据
* @param   queryFeaturesLength   -- [输入] 图片特征数据长度
* @param   distance              -- [输出] 特征数据差距。数字愈接近0 代表愈相似
* @return  QSTError 错误码
*/
QST_SDK_API QSTError QST_CompareFeatures(const char*         targetFeatures,
                                         const unsigned int  targetFeaturesLength,
                                         const char*         queryFeatures,
                                         const unsigned int  queryFeaturesLength,
                                         double*             distance
                                         );


}      /* extern "C" */
#endif /* ifdef __cplusplus */
#endif /* QST_CompareFeatures_h */