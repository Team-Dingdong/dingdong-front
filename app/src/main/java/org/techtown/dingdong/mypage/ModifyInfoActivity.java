package org.techtown.dingdong.mypage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.oss.licenses.OssLicensesActivity;
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;
import com.google.gson.Gson;

import org.techtown.dingdong.BuildConfig;
import org.techtown.dingdong.PrefManager;
import org.techtown.dingdong.R;
import org.techtown.dingdong.TutorialActivity;
import org.techtown.dingdong.home.EditActivity;
import org.techtown.dingdong.login_register.LoginActivity;
import org.techtown.dingdong.login_register.LoginOrRegisterActivity;
import org.techtown.dingdong.login_register.Token;
import org.techtown.dingdong.network.Api;
import org.techtown.dingdong.network.Apiinterface;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyInfoActivity extends AppCompatActivity {

    Button btn_modify, btn_correct, btn_quit, btn_logout, btn_auth;
    LinearLayout ln_modify, ln_auth;
    TextView tv_phone, tv_oss, tv_ossinfo;
    EditText et_modify, et_auth;
    ImageButton btn_back;
    private String phonenum, authnum;
    String license1 = "The MIT License\n" +
            "\n" +
            "Copyright (c) 2021 <copyright holders>\n" +
            "\n" +
            "Permission is hereby granted, free of charge, to any person obtaining a copy\n" +
            "of this software and associated documentation files (the \"Software\"), to deal\n" +
            "in the Software without restriction, including without limitation the rights\n" +
            "to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\n" +
            "copies of the Software, and to permit persons to whom the Software is\n" +
            "furnished to do so, subject to the following conditions:\n" +
            "\n" +
            "The above copyright notice and this permission notice shall be included in\n" +
            "all copies or substantial portions of the Software.\n" +
            "\n" +
            "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n" +
            "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\n" +
            "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\n" +
            "AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\n" +
            "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\n" +
            "OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN\n" +
            "THE SOFTWARE.";
    String license2 = "BSD 2-Clause FreeBSD License\n" +
            "The FreeBSD Copyright\n" +
            "\n" +
            "Copyright 1992-2012 The FreeBSD Project. All rights reserved.\n" +
            "\n" +
            "Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:\n" +
            "\n" +
            "   1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.\n" +
            "\n" +
            "   2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.\n" +
            "\n" +
            "THIS SOFTWARE IS PROVIDED BY THE FREEBSD PROJECT ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE FREEBSD PROJECT OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.\n" +
            "\n" +
            "The views and conclusions contained in the software and documentation are those of the authors and should not be interpreted as representing official policies, either expressed or implied, of the FreeBSD Project.";

    String license3 = "Apache License 2.0\n" +
            "Apache License\n" +
            "Version 2.0, January 2004\n" +
            "http://www.apache.org/licenses/\n" +
            "\n" +
            "TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION\n" +
            "\n" +
            "1. Definitions.\n" +
            "\n" +
            "\"License\" shall mean the terms and conditions for use, reproduction, and distribution as defined by Sections 1 through 9 of this document.\n" +
            "\n" +
            "\"Licensor\" shall mean the copyright owner or entity authorized by the copyright owner that is granting the License.\n" +
            "\n" +
            "\"Legal Entity\" shall mean the union of the acting entity and all other entities that control, are controlled by, or are under common control with that entity. For the purposes of this definition, \"control\" means (i) the power, direct or indirect, to cause the direction or management of such entity, whether by contract or otherwise, or (ii) ownership of fifty percent (50%) or more of the outstanding shares, or (iii) beneficial ownership of such entity.\n" +
            "\n" +
            "\"You\" (or \"Your\") shall mean an individual or Legal Entity exercising permissions granted by this License.\n" +
            "\n" +
            "\"Source\" form shall mean the preferred form for making modifications, including but not limited to software source code, documentation source, and configuration files.\n" +
            "\n" +
            "\"Object\" form shall mean any form resulting from mechanical transformation or translation of a Source form, including but not limited to compiled object code, generated documentation, and conversions to other media types.\n" +
            "\n" +
            "\"Work\" shall mean the work of authorship, whether in Source or Object form, made available under the License, as indicated by a copyright notice that is included in or attached to the work (an example is provided in the Appendix below).\n" +
            "\n" +
            "\"Derivative Works\" shall mean any work, whether in Source or Object form, that is based on (or derived from) the Work and for which the editorial revisions, annotations, elaborations, or other modifications represent, as a whole, an original work of authorship. For the purposes of this License, Derivative Works shall not include works that remain separable from, or merely link (or bind by name) to the interfaces of, the Work and Derivative Works thereof.\n" +
            "\n" +
            "\"Contribution\" shall mean any work of authorship, including the original version of the Work and any modifications or additions to that Work or Derivative Works thereof, that is intentionally submitted to Licensor for inclusion in the Work by the copyright owner or by an individual or Legal Entity authorized to submit on behalf of the copyright owner. For the purposes of this definition, \"submitted\" means any form of electronic, verbal, or written communication sent to the Licensor or its representatives, including but not limited to communication on electronic mailing lists, source code control systems, and issue tracking systems that are managed by, or on behalf of, the Licensor for the purpose of discussing and improving the Work, but excluding communication that is conspicuously marked or otherwise designated in writing by the copyright owner as \"Not a Contribution.\"\n" +
            "\n" +
            "\"Contributor\" shall mean Licensor and any individual or Legal Entity on behalf of whom a Contribution has been received by Licensor and subsequently incorporated within the Work.\n" +
            "\n" +
            "2. Grant of Copyright License. Subject to the terms and conditions of this License, each Contributor hereby grants to You a perpetual, worldwide, non-exclusive, no-charge, royalty-free, irrevocable copyright license to reproduce, prepare Derivative Works of, publicly display, publicly perform, sublicense, and distribute the Work and such Derivative Works in Source or Object form.\n" +
            "\n" +
            "3. Grant of Patent License. Subject to the terms and conditions of this License, each Contributor hereby grants to You a perpetual, worldwide, non-exclusive, no-charge, royalty-free, irrevocable (except as stated in this section) patent license to make, have made, use, offer to sell, sell, import, and otherwise transfer the Work, where such license applies only to those patent claims licensable by such Contributor that are necessarily infringed by their Contribution(s) alone or by combination of their Contribution(s) with the Work to which such Contribution(s) was submitted. If You institute patent litigation against any entity (including a cross-claim or counterclaim in a lawsuit) alleging that the Work or a Contribution incorporated within the Work constitutes direct or contributory patent infringement, then any patent licenses granted to You under this License for that Work shall terminate as of the date such litigation is filed.\n" +
            "\n" +
            "4. Redistribution. You may reproduce and distribute copies of the Work or Derivative Works thereof in any medium, with or without modifications, and in Source or Object form, provided that You meet the following conditions:\n" +
            "\n" +
            "     (a) You must give any other recipients of the Work or Derivative Works a copy of this License; and\n" +
            "\n" +
            "     (b) You must cause any modified files to carry prominent notices stating that You changed the files; and\n" +
            "\n" +
            "     (c) You must retain, in the Source form of any Derivative Works that You distribute, all copyright, patent, trademark, and attribution notices from the Source form of the Work, excluding those notices that do not pertain to any part of the Derivative Works; and\n" +
            "\n" +
            "     (d) If the Work includes a \"NOTICE\" text file as part of its distribution, then any Derivative Works that You distribute must include a readable copy of the attribution notices contained within such NOTICE file, excluding those notices that do not pertain to any part of the Derivative Works, in at least one of the following places: within a NOTICE text file distributed as part of the Derivative Works; within the Source form or documentation, if provided along with the Derivative Works; or, within a display generated by the Derivative Works, if and wherever such third-party notices normally appear. The contents of the NOTICE file are for informational purposes only and do not modify the License. You may add Your own attribution notices within Derivative Works that You distribute, alongside or as an addendum to the NOTICE text from the Work, provided that such additional attribution notices cannot be construed as modifying the License.\n" +
            "\n" +
            "     You may add Your own copyright statement to Your modifications and may provide additional or different license terms and conditions for use, reproduction, or distribution of Your modifications, or for any such Derivative Works as a whole, provided Your use, reproduction, and distribution of the Work otherwise complies with the conditions stated in this License.\n" +
            "\n" +
            "5. Submission of Contributions. Unless You explicitly state otherwise, any Contribution intentionally submitted for inclusion in the Work by You to the Licensor shall be under the terms and conditions of this License, without any additional terms or conditions. Notwithstanding the above, nothing herein shall supersede or modify the terms of any separate license agreement you may have executed with Licensor regarding such Contributions.\n" +
            "\n" +
            "6. Trademarks. This License does not grant permission to use the trade names, trademarks, service marks, or product names of the Licensor, except as required for reasonable and customary use in describing the origin of the Work and reproducing the content of the NOTICE file.\n" +
            "\n" +
            "7. Disclaimer of Warranty. Unless required by applicable law or agreed to in writing, Licensor provides the Work (and each Contributor provides its Contributions) on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied, including, without limitation, any warranties or conditions of TITLE, NON-INFRINGEMENT, MERCHANTABILITY, or FITNESS FOR A PARTICULAR PURPOSE. You are solely responsible for determining the appropriateness of using or redistributing the Work and assume any risks associated with Your exercise of permissions under this License.\n" +
            "\n" +
            "8. Limitation of Liability. In no event and under no legal theory, whether in tort (including negligence), contract, or otherwise, unless required by applicable law (such as deliberate and grossly negligent acts) or agreed to in writing, shall any Contributor be liable to You for damages, including any direct, indirect, special, incidental, or consequential damages of any character arising as a result of this License or out of the use or inability to use the Work (including but not limited to damages for loss of goodwill, work stoppage, computer failure or malfunction, or any and all other commercial damages or losses), even if such Contributor has been advised of the possibility of such damages.\n" +
            "\n" +
            "9. Accepting Warranty or Additional Liability. While redistributing the Work or Derivative Works thereof, You may choose to offer, and charge a fee for, acceptance of support, warranty, indemnity, or other liability obligations and/or rights consistent with this License. However, in accepting such obligations, You may act only on Your own behalf and on Your sole responsibility, not on behalf of any other Contributor, and only if You agree to indemnify, defend, and hold each Contributor harmless for any liability incurred by, or claims asserted against, such Contributor by reason of your accepting any such warranty or additional liability.\n" +
            "\n" +
            "END OF TERMS AND CONDITIONS\n" +
            "\n" +
            "APPENDIX: How to apply the Apache License to your work.\n" +
            "\n" +
            "To apply the Apache License to your work, attach the following boilerplate notice, with the fields enclosed by brackets \"[]\" replaced with your own identifying information. (Don't include the brackets!)  The text should be enclosed in the appropriate comment syntax for the file format. We also recommend that a file or class name and description of purpose be included on the same \"printed page\" as the copyright notice for easier identification within third-party archives.\n" +
            "\n" +
            "Copyright 2021 dingdong\n" +
            "\n" +
            "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
            "you may not use this file except in compliance with the License.\n" +
            "You may obtain a copy of the License at\n" +
            "\n" +
            "http://www.apache.org/licenses/LICENSE-2.0\n" +
            "\n" +
            "Unless required by applicable law or agreed to in writing, software\n" +
            "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
            "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
            "See the License for the specific language governing permissions and\n" +
            "limitations under the License.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_info);

        btn_modify = findViewById(R.id.btn_modify);
        btn_correct = findViewById(R.id.btn_correct);
        btn_quit = findViewById(R.id.btn_quit);
        ln_modify = findViewById(R.id.modify);
        et_modify = findViewById(R.id.et_modify);
        tv_phone = findViewById(R.id.tv_phone);
        btn_back = findViewById(R.id.ic_back);
        btn_logout = findViewById(R.id.btn_logout);
        ln_auth = findViewById(R.id.auth);
        et_auth = findViewById(R.id.et_auth);
        btn_auth = findViewById(R.id.btn_auth);
        tv_oss = findViewById(R.id.tv_oss);
        tv_ossinfo = findViewById(R.id.tv_ossinfo);

        SharedPreferences pref = this.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        String access = pref.getString("oauth.accesstoken", "");
        String refresh = pref.getString("oauth.refreshtoken", "");
        String expire = pref.getString("oauth.expire", "");
        String tokentype = pref.getString("oauth.tokentype", "");

        Token token = new Token(access, refresh, expire, tokentype);
        token.setContext(ModifyInfoActivity.this);

        tv_ossinfo.setText(license3);


        btn_correct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ln_modify.setVisibility(View.VISIBLE);
                ln_auth.setVisibility(View.GONE);
            }
        });

        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_modify.getText().toString().length() == 11){
                    phonenum = et_modify.getText().toString();
                    ln_modify.setVisibility(View.GONE);
                    ln_auth.setVisibility(View.VISIBLE);
                    btn_correct.setText("다시받기");
                }else{
                    Toast.makeText(ModifyInfoActivity.this,"11자리 전화번호를 입력해주세요.",Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //탈퇴하기 액티비티로
                //startActivity(new Intent(ModifyInfoActivity.this, LeaveActivity.class));

                //탈퇴하기 다이얼로그를 띄운다
                AlertDialog.Builder dialog = new AlertDialog.Builder(ModifyInfoActivity.this);

                dialog.setMessage("계정의 모든 정보가 삭제됩니다. \n정말로 탈퇴하시겠습니까?")
                        .setTitle("탈퇴하기")
                        .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("Dialog", "아니오");
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //네를 눌렀을시
                                Log.i("Dialog", "네");
                                Apiinterface apiinterface = Api.createService(Apiinterface.class, token, ModifyInfoActivity.this);
                                Call<ResponseBody> call = apiinterface.leaveUser();
                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful()) {
                                            if (response.code() == 200) {
                                                pref.edit().putBoolean("oauth.loggedin",false).apply();
                                                pref.edit().putString("oauth.accesstoken", "").apply();
                                                pref.edit().putString("oauth.refreshtoken", "").apply();
                                                pref.edit().putString("oauth.expire", "").apply();
                                                pref.edit().putString("oauth.tokentype", "").apply();

                                                Toast.makeText(ModifyInfoActivity.this, "탈퇴가 완료되었습니다.", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(ModifyInfoActivity.this, TutorialActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            }

                                        } else {

                                            Log.d("실패", new Gson().toJson(response.errorBody()));
                                            Log.d("실패", response.toString());
                                            Log.d("실패", String.valueOf(response.code()));
                                            Log.d("실패", response.message());
                                            Log.d("실패", String.valueOf(response.raw().request().url().url()));
                                            Log.d("실패", new Gson().toJson(response.raw().request()));

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });

                            }
                        }).show();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_auth.getText().toString().length() == 6){
                    authnum = et_auth.getText().toString();
                    Toast.makeText(ModifyInfoActivity.this,"전화번호 수정이 완료되었습니다.",Toast.LENGTH_LONG).show();
                    ln_auth.setVisibility(View.GONE);
                    btn_correct.setText("수정하기");
                    tv_phone.setText(phonenum);

                }
                else{
                    Toast.makeText(ModifyInfoActivity.this,"6자리 인증번호를 입력해주세요.",Toast.LENGTH_LONG).show();
                }
            }
        });

        tv_oss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ModifyInfoActivity.this, OssLicensesMenuActivity.class));
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ModifyInfoActivity.this);

                dialog.setMessage("정말로 로그아웃하시겠습니까?")
                        .setTitle("로그아웃")
                        .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("Dialog", "아니오");
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("Dialog", "네");

                                Apiinterface apiinterface = Api.createService(Apiinterface.class, token, ModifyInfoActivity.this);
                                Call<ResponseBody> call = apiinterface.logoutUser();
                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful()) {
                                            if (response.code() == 200) {
                                                pref.edit().putBoolean("oauth.loggedin",false).apply();
                                                pref.edit().putString("oauth.accesstoken", "").apply();
                                                pref.edit().putString("oauth.refreshtoken", "").apply();
                                                pref.edit().putString("oauth.expire", "").apply();
                                                pref.edit().putString("oauth.tokentype", "").apply();
                                                Toast.makeText(ModifyInfoActivity.this, "로그아웃이 완료되었습니다.", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(ModifyInfoActivity.this, TutorialActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            }

                                        } else {

                                            Log.d("실패", new Gson().toJson(response.errorBody()));
                                            Log.d("실패", response.toString());
                                            Log.d("실패", String.valueOf(response.code()));
                                            Log.d("실패", response.message());
                                            Log.d("실패", String.valueOf(response.raw().request().url().url()));
                                            Log.d("실패", new Gson().toJson(response.raw().request()));

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                            }
                        }).show();
            }
        });


    }
}