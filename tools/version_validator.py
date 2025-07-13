#!/usr/bin/env python3
"""
Version Number Validator

This script validates version numbers and extracts major.minor.patch format.

Version format rules:
- Can start with 'v' (optional): v2.3.1 or 2.3.1
- Must have major.minor.patch: 1.2.3 (minimum required)
- May have build number: 1.2.3-1 or 1.2.3-2
- May end with -SNAPSHOT: 3.4.5-1-SNAPSHOT or 3.4.5-SNAPSHOT

Usage:
    python version_validator.py <version_string>
    python version_validator.py --extract <version_string>
"""

import re
import sys
import argparse

def extract_major_minor_patch(version_string):
    """
    Extract major.minor.patch from a valid version string in vX.Y.Z format.

    Args:
        version_string (str): The version string to extract from

    Returns:
        str: The extracted version in vX.Y.Z format, or None if invalid
    """
    # Define the regex pattern for valid version strings
    pattern = r'^v?(\d+)\.(\d+)\.(\d+)(?:-(\d+))?(?:-SNAPSHOT)?$'
    match = re.match(pattern, version_string)

    if match:
        major, minor, patch = match.groups()[:3]
        return f"v{major}.{minor}.{patch}"

    return None

def validate_version(version_string):
    """
    Validate a version string according to the specified rules.

    Args:
        version_string (str): The version string to validate

    Returns:
        bool: True if valid, False otherwise
    """
    return extract_major_minor_patch(version_string) is not None

def main():
    parser = argparse.ArgumentParser(
        description="Validate version number and extract major.minor.patch format",
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
Examples:
    python version_validator.py v2.3.1                     # Validate version
    python version_validator.py 1.2.3-1                    # Validate version with build
    python version_validator.py --extract v2.3.1-SNAPSHOT  # Extract v2.3.1
    python version_validator.py -e 3.4.5-2-SNAPSHOT        # Extract v3.4.5

Valid version formats:
    v1.2.3, 1.2.3, v1.2.3-1, 1.2.3-1, v1.2.3-SNAPSHOT, 1.2.3-1-SNAPSHOT
        """
    )

    parser.add_argument(
        'version', 
        help='Version string to validate/extract'
    )
    parser.add_argument(
        '-e', '--extract', 
        action='store_true',
        help='Extract major.minor.patch in vX.Y.Z format'
    )

    args = parser.parse_args()

    if args.extract:
        # Extract mode
        result = extract_major_minor_patch(args.version)
        if result:
            print(result)
            sys.exit(0)
        else:
            print(f"Error: '{args.version}' is not a valid version string", file=sys.stderr)
            sys.exit(1)
    else:
        # Validation mode
        if validate_version(args.version):
            print(f"'{args.version}' is a valid version string")
            sys.exit(0)
        else:
            print(f"Error: '{args.version}' is not a valid version string", file=sys.stderr)
            sys.exit(1)


if __name__ == "__main__":
    # If no arguments provided, run some test cases
    if len(sys.argv) == 1:
        print("Running test cases...")

        test_cases = [
            # Valid cases
            ("v2.3.1", True),
            ("2.3.1", True),
            ("1.2.3-1", True),
            ("v1.2.3-1", True),
            ("3.4.5-SNAPSHOT", True),
            ("v3.4.5-SNAPSHOT", True),
            ("1.2.3-1-SNAPSHOT", True),
            ("v1.2.3-1-SNAPSHOT", True),
            ("10.20.30", True),
            ("v0.0.1", True),

            # Invalid cases
            ("1.2", False),
            ("v1.2", False),
            ("1.2.3.4", False),
            ("v1.2.3.4", False),
            ("1.2.3-", False),
            ("1.2.3-SNAPSHOT-1", False),
            ("1.2.3-a", False),
            ("v1.2.3-a", False),
            ("av1.2.3", False),
            ("a1.2.3", False),
            (" v1.2.3", False),
            ("V1.2.3-a", False),
            ("1.2.x", False),
            ("", False),
            ("v", False),
            ("1.2.3-1-SNAPSHOT-extra", False),
        ]

        print("\nValidation Tests:")
        for version, expected in test_cases:
            result = validate_version(version)
            status = "✓" if result == expected else "✗"
            print(f"{status} {version:<25} -> {result} (expected {expected})")

        print("\nExtraction Tests:")
        extraction_tests = [
            ("v2.3.1", "v2.3.1"),
            ("2.3.1", "v2.3.1"),
            ("1.2.3-1", "v1.2.3"),
            ("v1.2.3-1-SNAPSHOT", "v1.2.3"),
            ("3.4.5-SNAPSHOT", "v3.4.5"),
            ("invalid", None),
        ]

        for version, expected in extraction_tests:
            result = extract_major_minor_patch(version)
            status = "✓" if result == expected else "✗"
            print(f"{status} {version:<25} -> {result} (expected {expected})")
    else:
        main()
