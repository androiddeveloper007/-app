
package com.zp.mobile91q.videoplayer.subtitle;

/**
 * 字幕已知格式(目前3128设备只解析smi,ssa,ass,srt四种格式)
 */
public enum SUBTYPE {
    SUB_INVALID,
    SUB_MICRODVD,
    SUB_SUBRIP,
    SUB_SUBVIEWER,
    SUB_SMI, // smi
    SUB_VPLAYER,
    SUB_SRT, // srt
    SUB_SSA, // ssa
    SUB_ASS, // ass
    SUB_TXT, // txt
    SUB_PJS,
    SUB_MPSUB,
    SUB_AQTITLE,
    SUB_SUBVIEWER2,
    SUB_SUBVIEWER3,
    SUB_SUBRIP09,
    SUB_JACOSUB,
    SUB_MPL2,
    SUB_DIVX,
    SUB_IDXSUB, // idx,sub
    SUB_COMMONTXT,
    SUB_LRC,
    INSUB
}
