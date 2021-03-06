/* Copyright Statement:
 *
 * This software/firmware and related documentation ("MediaTek Software") are
 * protected under relevant copyright laws. The information contained herein is
 * confidential and proprietary to MediaTek Inc. and/or its licensors. Without
 * the prior written permission of MediaTek inc. and/or its licensors, any
 * reproduction, modification, use or disclosure of MediaTek Software, and
 * information contained herein, in whole or in part, shall be strictly
 * prohibited.
 * 
 * MediaTek Inc. (C) 2010. All rights reserved.
 * 
 * BY OPENING THIS FILE, RECEIVER HEREBY UNEQUIVOCALLY ACKNOWLEDGES AND AGREES
 * THAT THE SOFTWARE/FIRMWARE AND ITS DOCUMENTATIONS ("MEDIATEK SOFTWARE")
 * RECEIVED FROM MEDIATEK AND/OR ITS REPRESENTATIVES ARE PROVIDED TO RECEIVER
 * ON AN "AS-IS" BASIS ONLY. MEDIATEK EXPRESSLY DISCLAIMS ANY AND ALL
 * WARRANTIES, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NONINFRINGEMENT. NEITHER DOES MEDIATEK PROVIDE ANY WARRANTY WHATSOEVER WITH
 * RESPECT TO THE SOFTWARE OF ANY THIRD PARTY WHICH MAY BE USED BY,
 * INCORPORATED IN, OR SUPPLIED WITH THE MEDIATEK SOFTWARE, AND RECEIVER AGREES
 * TO LOOK ONLY TO SUCH THIRD PARTY FOR ANY WARRANTY CLAIM RELATING THERETO.
 * RECEIVER EXPRESSLY ACKNOWLEDGES THAT IT IS RECEIVER'S SOLE RESPONSIBILITY TO
 * OBTAIN FROM ANY THIRD PARTY ALL PROPER LICENSES CONTAINED IN MEDIATEK
 * SOFTWARE. MEDIATEK SHALL ALSO NOT BE RESPONSIBLE FOR ANY MEDIATEK SOFTWARE
 * RELEASES MADE TO RECEIVER'S SPECIFICATION OR TO CONFORM TO A PARTICULAR
 * STANDARD OR OPEN FORUM. RECEIVER'S SOLE AND EXCLUSIVE REMEDY AND MEDIATEK'S
 * ENTIRE AND CUMULATIVE LIABILITY WITH RESPECT TO THE MEDIATEK SOFTWARE
 * RELEASED HEREUNDER WILL BE, AT MEDIATEK'S OPTION, TO REVISE OR REPLACE THE
 * MEDIATEK SOFTWARE AT ISSUE, OR REFUND ANY SOFTWARE LICENSE FEES OR SERVICE
 * CHARGE PAID BY RECEIVER TO MEDIATEK FOR SUCH MEDIATEK SOFTWARE AT ISSUE.
 *
 * The following software/firmware and/or related documentation ("MediaTek
 * Software") have been modified by MediaTek Inc. All revisions are subject to
 * any receiver's applicable license agreements with MediaTek Inc.
 */

package org.bouncycastle.jce.provider.asymmetric.ec;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.ECPublicKey;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DSA;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.NullDigest;
// BEGIN android-removed
// import org.bouncycastle.crypto.digests.RIPEMD160Digest;
// END android-removed
import org.bouncycastle.crypto.digests.SHA1Digest;
// BEGIN android-removed
// import org.bouncycastle.crypto.digests.SHA224Digest;
// END android-removed
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.SHA384Digest;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.signers.ECDSASigner;
// BEGIN android-removed
// import org.bouncycastle.crypto.signers.ECNRSigner;
// END android-removed
import org.bouncycastle.jce.interfaces.ECKey;
import org.bouncycastle.jce.provider.DSABase;
import org.bouncycastle.jce.provider.DSAEncoder;
import org.bouncycastle.jce.provider.JDKKeyFactory;

public class Signature
    extends DSABase
{
    Signature(Digest digest, DSA signer, DSAEncoder encoder)
    {
        super(digest, signer, encoder);
    }

    protected void engineInitVerify(PublicKey publicKey)
        throws InvalidKeyException
    {
        CipherParameters param;

        if (publicKey instanceof ECPublicKey)
        {
            param = ECUtil.generatePublicKeyParameter(publicKey);
        }
        else
        {
            try
            {
                byte[] bytes = publicKey.getEncoded();

                publicKey = JDKKeyFactory.createPublicKeyFromDERStream(bytes);

                if (publicKey instanceof ECPublicKey)
                {
                    param = ECUtil.generatePublicKeyParameter(publicKey);
                }
                else
                {
                    throw new InvalidKeyException("can't recognise key type in ECDSA based signer");
                }
            }
            catch (Exception e)
            {
                throw new InvalidKeyException("can't recognise key type in ECDSA based signer");
            }
        }

        digest.reset();
        signer.init(false, param);
    }

    protected void engineInitSign(
        PrivateKey privateKey,
        SecureRandom random)
        throws InvalidKeyException
    {
        CipherParameters param;

        if (privateKey instanceof ECKey)
        {
            param = ECUtil.generatePrivateKeyParameter(privateKey);
        }
        else
        {
            throw new InvalidKeyException("can't recognise key type in ECDSA based signer");
        }

        digest.reset();

        if (random != null)
        {
            signer.init(true, new ParametersWithRandom(param, random));
        }
        else
        {
            signer.init(true, param);
        }
    }

    static public class ecDSA
        extends Signature
    {
        public ecDSA()
        {
            super(new SHA1Digest(), new ECDSASigner(), new StdDSAEncoder());
        }
    }

    static public class ecDSAnone
        extends Signature
    {
        public ecDSAnone()
        {
            super(new NullDigest(), new ECDSASigner(), new StdDSAEncoder());
        }
    }

    // BEGIN android-removed
    // static public class ecDSA224
    //     extends Signature
    // {
    //     public ecDSA224()
    //     {
    //         super(new SHA224Digest(), new ECDSASigner(), new StdDSAEncoder());
    //     }
    // }
    // END android-removed

    static public class ecDSA256
        extends Signature
    {
        public ecDSA256()
        {
            super(new SHA256Digest(), new ECDSASigner(), new StdDSAEncoder());
        }
    }

    static public class ecDSA384
        extends Signature
    {
        public ecDSA384()
        {
            super(new SHA384Digest(), new ECDSASigner(), new StdDSAEncoder());
        }
    }

    static public class ecDSA512
        extends Signature
    {
        public ecDSA512()
        {
            super(new SHA512Digest(), new ECDSASigner(), new StdDSAEncoder());
        }
    }

    // BEGIN android-removed
    // static public class ecDSARipeMD160
    //     extends Signature
    // {
    //     public ecDSARipeMD160()
    //     {
    //         super(new RIPEMD160Digest(), new ECDSASigner(), new StdDSAEncoder());
    //     }
    // }
    //
    // static public class ecNR
    //     extends Signature
    // {
    //     public ecNR()
    //     {
    //         super(new SHA1Digest(), new ECNRSigner(), new StdDSAEncoder());
    //     }
    // }
    //
    // static public class ecNR224
    //     extends Signature
    // {
    //     public ecNR224()
    //     {
    //         super(new SHA224Digest(), new ECNRSigner(), new StdDSAEncoder());
    //     }
    // }
    //
    // static public class ecNR256
    //     extends Signature
    // {
    //     public ecNR256()
    //     {
    //         super(new SHA256Digest(), new ECNRSigner(), new StdDSAEncoder());
    //     }
    // }
    //
    // static public class ecNR384
    //     extends Signature
    // {
    //     public ecNR384()
    //     {
    //         super(new SHA384Digest(), new ECNRSigner(), new StdDSAEncoder());
    //     }
    // }
    //
    // static public class ecNR512
    //     extends Signature
    // {
    //     public ecNR512()
    //     {
    //         super(new SHA512Digest(), new ECNRSigner(), new StdDSAEncoder());
    //     }
    // }
    //
    // static public class ecCVCDSA
    //     extends Signature
    // {
    //     public ecCVCDSA()
    //     {
    //         super(new SHA1Digest(), new ECDSASigner(), new CVCDSAEncoder());
    //     }
    // }
    //
    // static public class ecCVCDSA224
    //     extends Signature
    // {
    //     public ecCVCDSA224()
    //     {
    //         super(new SHA224Digest(), new ECDSASigner(), new CVCDSAEncoder());
    //     }
    // }
    //
    // static public class ecCVCDSA256
    //     extends Signature
    // {
    //     public ecCVCDSA256()
    //     {
    //         super(new SHA256Digest(), new ECDSASigner(), new CVCDSAEncoder());
    //     }
    // }
    // END android-removed

    private static class StdDSAEncoder
        implements DSAEncoder
    {
        public byte[] encode(
            BigInteger r,
            BigInteger s)
            throws IOException
        {
            ASN1EncodableVector v = new ASN1EncodableVector();

            v.add(new DERInteger(r));
            v.add(new DERInteger(s));

            return new DERSequence(v).getEncoded(ASN1Encodable.DER);
        }

        public BigInteger[] decode(
            byte[] encoding)
            throws IOException
        {
            ASN1Sequence s = (ASN1Sequence)ASN1Object.fromByteArray(encoding);
            BigInteger[] sig = new BigInteger[2];

            sig[0] = ((DERInteger)s.getObjectAt(0)).getValue();
            sig[1] = ((DERInteger)s.getObjectAt(1)).getValue();

            return sig;
        }
    }

    private static class CVCDSAEncoder
        implements DSAEncoder
    {
        public byte[] encode(
            BigInteger r,
            BigInteger s)
            throws IOException
        {
            byte[] first = makeUnsigned(r);
            byte[] second = makeUnsigned(s);
            byte[] res;

            if (first.length > second.length)
            {
                res = new byte[first.length * 2];
            }
            else
            {
                res = new byte[second.length * 2];
            }

            System.arraycopy(first, 0, res, res.length / 2 - first.length, first.length);
            System.arraycopy(second, 0, res, res.length - second.length, second.length);

            return res;
        }


        private byte[] makeUnsigned(BigInteger val)
        {
            byte[] res = val.toByteArray();

            if (res[0] == 0)
            {
                byte[] tmp = new byte[res.length - 1];

                System.arraycopy(res, 1, tmp, 0, tmp.length);

                return tmp;
            }

            return res;
        }

        public BigInteger[] decode(
            byte[] encoding)
            throws IOException
        {
            BigInteger[] sig = new BigInteger[2];

            byte[] first = new byte[encoding.length / 2];
            byte[] second = new byte[encoding.length / 2];

            System.arraycopy(encoding, 0, first, 0, first.length);
            System.arraycopy(encoding, first.length, second, 0, second.length);

            sig[0] = new BigInteger(1, first);
            sig[1] = new BigInteger(1, second);

            return sig;
        }
    }
}
