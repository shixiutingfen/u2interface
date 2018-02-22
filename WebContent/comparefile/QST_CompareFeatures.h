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
*                                          QST ������
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
* �Ƚ���������
* @param   targetFeatures        -- [����] ��������
* @param   targetFeaturesLength  -- [����] ͼƬ�������ݳ���
* @param   queryFeatures         -- [����] ��������
* @param   queryFeaturesLength   -- [����] ͼƬ�������ݳ���
* @param   distance              -- [���] �������ݲ�ࡣ�������ӽ�0 ����������
* @return  QSTError ������
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