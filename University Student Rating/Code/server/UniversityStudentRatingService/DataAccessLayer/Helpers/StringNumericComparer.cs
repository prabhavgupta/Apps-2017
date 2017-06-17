using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccessLayer.Helpers
{
    public class StringNumericComparer : IComparer<string>
    {
        public int Compare(string s1, string s2)
        {
            if (IsNumeric(s1) && IsNumeric(s2))
            {
                if (Convert.ToDouble(s1) > Convert.ToDouble(s2)) return 1;
                if (Convert.ToDouble(s1) < Convert.ToDouble(s2)) return -1;
                if (Convert.ToDouble(s1) == Convert.ToDouble(s2)) return 0;
            }
            return string.Compare(s1, s2);
        }

        private static bool IsNumeric(string value)
        {
            try
            {
                double numeric = Convert.ToDouble(value);
                return true;
            }
            catch (FormatException)
            {
                return false;
            }
        }
    }
}
